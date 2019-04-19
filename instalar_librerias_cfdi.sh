# XXX: https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
# XXX: https://www.javamexico.org/blogs/nomarlegnar/leer_xml_de_un_cfdi_para_darle_el_tratamiento_que_quieras_usando_xmlbeans
# XXX: https://www.javamexico.org/blogs/nomarlegnar/cfdi_con_java_y_xmlbeans?page=1
mvn install:install-file -Dfile=cfdv32.jar -DgroupId=org.nada -DartifactId=cfdi -Dversion=3.2 -Dpackaging=jar
mvn install:install-file -Dfile=cfdi33.jar -DgroupId=org.nada -DartifactId=cfdi -Dversion=3.3 -Dpackaging=jar
