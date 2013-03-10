cmd /C mvn install:install-file -Dfile=jocl.jar -DgroupId=net.java.dev.jogl -DartifactId=jocl -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=jogl.all.jar -DgroupId=net.java.dev.jogl -DartifactId=jogl-all -Dversion=2.0-rc3 -Dpackaging=jar

cmd /C mvn install:install-file -Dfile=jogl-all-natives-linux-amd64.jar -DgroupId=net.java.dev.jogl -DartifactId=jogl-all-natives-linux-amd64 -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=jogl-all-natives-linux-i586.jar -DgroupId=net.java.dev.jogl -DartifactId=jogl-all-natives-linux-i586 -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=jogl-all-natives-windows-i586.jar -DgroupId=net.java.dev.jogl -DartifactId=jogl-windows-i586 -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=jogl-all-natives-windows-amd64.jar -DgroupId=net.java.dev.jogl -DartifactId=jogl-windows-amd64 -Dversion=2.0-rc3 -Dpackaging=jar

cmd /C mvn install:install-file -Dfile=gluegen.jar -DgroupId=net.java.dev.jogl -DartifactId=gluegen -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=gluegen-rt.jar -DgroupId=net.java.dev.jogl -DartifactId=gluegen-rt -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=gluegen-rt-natives-windows-i586.jar -DgroupId=net.java.dev.jogl -DartifactId=gluegen-rt-natives-windows-i586 -Dversion=2.0-rc3 -Dpackaging=jar
cmd /C mvn install:install-file -Dfile=gluegen-rt-natives-windows-amd64.jar -DgroupId=net.java.dev.jogl -DartifactId=gluegen-rt-natives-windows-amd64 -Dversion=2.0-rc3 -Dpackaging=jar
