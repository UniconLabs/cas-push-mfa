FROM openjdk:8

COPY target/cas.war /opt/cas/cas.war
COPY etc/cas/ /etc/cas/

#RUN keytool -import -alias tomcat -keystore /etc/ssl/certs/java/cacerts -file /etc/cas/tomcat.der -storepass changeit -noprompt

EXPOSE 8443

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "/opt/cas/cas.war"]
