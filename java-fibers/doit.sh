mvn clean package -P sunjdk8 && (cd target; java -Xmx412m -classpath /home/bernard/.m2/repository/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar:java-1.8-fibers-1.4.0.jar:/home/bernard/.m2/repository/co/paralleluniverse/quasar-core/0.7.9/quasar-core-0.7.9-jdk8.jar:/home/bernard/.m2/repository/com/esotericsoftware/kryo/4.0.0/kryo-4.0.0.jar:/home/bernard/.m2/repository/com/googlecode/concurrentlinkedhashmap/concurrentlinkedhashmap-lru/1.4/concurrentlinkedhashmap-lru-1.4.jar:/home/bernard/.m2/repository/com/google/collections/google-collections/1.0/google-collections-1.0.jar:/home/bernard/.m2/repository/com/google/guava/guava/20.0/guava-20.0.jar -javaagent:/home/bernard/.m2/repository/co/paralleluniverse/quasar-core/0.7.9/quasar-core-0.7.9-jdk8.jar=mb  bernard.tatin.fibers.PlayWithFibers > /dev/null)
