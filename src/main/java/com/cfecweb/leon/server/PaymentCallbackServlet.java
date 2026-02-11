package com.cfecweb.leon.server;

import com.cfecweb.leon.client.model.PaymentCallback;
import com.cfecweb.leon.client.model.PaymentCallback200Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cfecweb.leon.server.getDataImpl.PAYMENTS_API;

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
        try {
            Map<String, String[]> postParametersMap = req.getParameterMap();
            Map<String, List<String>> postParameterListMap = new HashMap<>();
            for (Map.Entry<String, String[]> entry : postParametersMap.entrySet()) {
                postParameterListMap.put(entry.getKey(), List.of(entry.getValue()));
            }

            PaymentCallback200Response response =
                    PAYMENTS_API.paymentCallback(new PaymentCallback().postParametersMap(postParameterListMap));

            String reqReferenceNumber = response.getRef();
            String ref = URLEncoder.encode(reqReferenceNumber, StandardCharsets.UTF_8);
            String url = req.getContextPath() + "/?action=confirm&ref=" + ref;
            resp.sendRedirect(resp.encodeRedirectURL(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
