package com.cfecweb.leon.server;

import com.cfecweb.leon.AppProperties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.cfecweb.leon.AppProperties.CYBERSOURCE_SECRET_KEY;
import static com.cfecweb.leon.server.ProcessOrder.CYBERSOURCE_SECRET_KEY_ENV;
import static com.cfecweb.leon.server.ProcessOrder.getEnvOrDefault;

public class PaymentCallbackServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Only allow POST. Everything else returns 405 with Allow header.
        if (!"POST".equalsIgnoreCase(req.getMethod())) {
            resp.setHeader("Allow", "POST");
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only POST is allowed on this endpoint.");
            return;
        }
        // Delegate to doPost
        super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> allParameters = new HashMap<>();

        Map<String, String[]> postParametersMap = req.getParameterMap();
        for (Map.Entry<String, String[]> e : postParametersMap.entrySet()) {
            String name = e.getKey();
            String[] vals = e.getValue();

            allParameters.put(name, vals[0]);
        }

        String signedFieldNamesStr = allParameters.get("signed_field_names");
        String[] signedFieldNames = signedFieldNamesStr.split(",");

        Map<String, String> signedFields = new LinkedHashMap<>();
        for (String signedFieldName : signedFieldNames) {
            signedFields.put(signedFieldName, allParameters.get(signedFieldName));
        }

        String secretKey = getEnvOrDefault(CYBERSOURCE_SECRET_KEY_ENV, AppProperties.get(CYBERSOURCE_SECRET_KEY));
        try {
            String signature = CybersourceSecurity.sign(signedFields, secretKey);
            boolean signatureOk = signature.equalsIgnoreCase(allParameters.get("signature"));

            if (!signatureOk) {
                // Signature check failed
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Signature check failed");
                return;
            } else {
                final String paymentData = getDataImpl.leonprop.getProperty("LEON.paymentData.Location", "/webapps/LEON/Prod/PaymentData");
                String reqReferenceNumber = signedFields.get("req_reference_number");

                // Load the payment processing context saved previously in ProcessOrder.createOrderProcessingPrerequisites
                File paymentDataFolder = new File(paymentData);
                if (!paymentDataFolder.exists()) {
                    // Fatal: Configuration error
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal: Configuration error");
                    return;
                }
                File paymentDataFile = new File(paymentDataFolder, reqReferenceNumber + ".json");
                if (!paymentDataFile.exists()) {
                    // Error 404
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found");
                    return;
                }
                File processedPaymentDataFile = new File(paymentDataFolder, reqReferenceNumber + ".processed.json");
                if (processedPaymentDataFile.exists()) {
                    // Error 409 Conflict — payment already processed
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Conflict: Payment has already been processed");
                    return;
                }

                // Load previous context, update it with received parameters, and save it to *.processed.json"
                StoredPaymentContext storedPaymentContext = StoredPaymentContext.load(paymentDataFile);
                storedPaymentContext.setPostParameters(allParameters);
                StoredPaymentContext.save(processedPaymentDataFile, storedPaymentContext);

                String ref = URLEncoder.encode(reqReferenceNumber, StandardCharsets.UTF_8);
                String url = req.getContextPath() + "/?action=confirm&ref=" + ref;

                resp.sendRedirect(resp.encodeRedirectURL(url));
            }
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
