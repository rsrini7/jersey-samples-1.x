point eclipse maven to the installed dir instead of the embedded.

add below to the .m2/settings.xml 

<profile>
	<id>sriniProfile</id>
	 <repositories>
		<repository>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
			<id>java.net-Public</id>
			<name>Maven Java Net Snapshots and Releases</name>
			<url>https://maven.java.net/content/groups/public/</url>
		</repository>
		<repository>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
            <id>repository.jboss.org</id>
            <name>jboss Maven Repository</name>
            <url>http://repository.jboss.org/nexus/content/groups/public/</url>
            <layout>default</layout>
        </repository>
        <repository>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
            <id>eclipselink.repository</id>
            <name>Eclipse Maven Repository</name>
            <url>http://www.eclipse.org/downloads/download.php?r=1&amp;nf=1&amp;file=/rt/eclipselink/maven.repo</url>
            <layout>default</layout>
        </repository>
		<repository>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
            <id>cloudera.repository</id>
            <name>Cloudera Maven Repository</name>
            <url>https://repository.cloudera.com/cloudera/java.net.m2-cache/</url>
            <layout>default</layout>
        </repository>
    </repositories>
	</profile>
  </profiles>

  
  <!-- activeProfiles
   | List of profiles that are active for all builds.
   |-->
  <activeProfiles>
    <activeProfile>sriniProfile</activeProfile>
  </activeProfiles>