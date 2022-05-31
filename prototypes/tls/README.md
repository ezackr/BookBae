- `openssl req -newkey rsa:2048 -nodes -keyout bookbae.key -out bookbae.csr`
  - makes a key and a request to sign the key, which will be consumed by a CA (us, later)
- `openssl req -x509 -sha256 -days 1825 -newkey rsa:2048 -keyout bookbaeCA.key -out bookbaeCA.crt`
  - makes our Bookbae Certificate Authority, which will be bundled with our app
- `openssl x509 -req -CA bookbaeCA.crt -CAkey bookbaeCA.key -in bookbae.csr -out bookbae.crt -days 900 -CAcreateserial -extfile file.cnf`
  - makes a certificate from our request and our CA
  - file.cnf should be certificate.cnf, localhost.cnf or some other config file depending on environment


- `keytool -importkeystore -srckeystore /etc/ssl/certs/java/cacerts -destkeystore cacerts.jks -srcstorepass changeit -deststorepass changeit`
  - imports the system's trusted CA's to Glassfish's trusted CA's
  - only need to do this once
  - or, if it causes issues, empty the cacerts.jks file of imported certificates and re-import the (possibly-updated) system certificates
- `openssl pkcs12 -export -in bookbae.crt -inkey key.pem -out bookbae.p12 -name bookbae -CAfile bookbaeCA.crt -caname "Bookbae CA"`
  - creates a package of our certificates that may be imported by Glassfish
- `keytool -importkeystore -deststorepass changeit -destkeystore keystore.jks -srckeystore bookbae.p12 -srcstoretype PKCS12 -srcstorepass changeit -alias bookbae`
  - Imports the keys into the Glassfish keystore


- Open the Glassfish admin console with `bin/asadmin`
  - `delete-ssl --type http-listener http-listener-2`
    - Choose the http-listener that is currently configured for ssl, usually http-listener-2
  - `stop-domain`
  - `start-domain`
  - `create-ssl --type http-listener --certname bookbae http-listener-2`
    - certname must be the same as the alias set in the keytool importkeystore command that operated on bookbae.p12
  - `stop-domain`
  - `start-domain`
- `cp -pv bookbaeCA.crt ../../frontend/BookBae/android/app/src/main/res/raw/`
  - Move the CA Certificate to the Android app build so it can incorporate it
