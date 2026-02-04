# The CFEC Online Renewal Application (LEON)



## Local build instructions

### Prerequisite: Install Local JARs into Maven Repository
"C:\Users\jamirov\projects\cfec-int-fs-leon\old-jars\"
If you haven't done this already, please run the following commands before building the project locally to install local JARs into Maven repo
```
mvn install:install-file -Dfile="old-jars/jboss-serialization-1.0.3.GA.jar" -DgroupId="jboss" -DartifactId=jboss-serialization -Dversion="1.0.3.GA" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/jboss-jpa-deployers-1.0.0-CR1.jar" -DgroupId="org.jboss.jpa" -DartifactId=jboss-jpa-deployers -Dversion="1.0.0-CR1" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/gxt-gpl-2.3.1a18-gwt210.jar" -DgroupId="com.extjs" -DartifactId=gxt-gpl -Dversion="2.3.1a18-gwt210" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/pvjdbc2-9.52.jar" -DgroupId="com.pervasive" -DartifactId=pvjdbc2 -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/configcli-9.52.jar" -DgroupId="com.pervasive" -DartifactId=configcli -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/javadti-9.52.jar" -DgroupId="com.pervasive" -DartifactId=javadti -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/jpscs-9.52.jar" -DgroupId="com.pervasive" -DartifactId=jpscs -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/monitorcli-9.52.jar" -DgroupId="com.pervasive" -DartifactId=monitorcli -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/psql-9.52.jar" -DgroupId="com.pervasive" -DartifactId=psql -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/pvjdbc2x-9.52.jar" -DgroupId="com.pervasive" -DartifactId=pvjdbc2x -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/startup-9.52.jar" -DgroupId="com.pervasive" -DartifactId=startup -Dversion="9.52" -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -Dfile="old-jars/ostermillerutils-1.04.03.jar" -DgroupId="com.Ostermiller" -DartifactId=ostermillerutils -Dversion="1.04.03" -Dpackaging=jar -DgeneratePom=true
```

### Build the Project

Once the dependencies are installed, you can build the project using:
```
mvn clean install
```
