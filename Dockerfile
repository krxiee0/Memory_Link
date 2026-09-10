<resources>

    <resource>
        <directory>${project.basedir}</directory>
        <includes>
            <include>application.properties</include>
        </includes>
    </resource>

    <resource>
        <directory>${project.basedir}</directory>
        <includes>
            <include>*.html</include>
        </includes>
        <targetPath>templates</targetPath>
    </resource>

    <resource>
        <directory>${project.basedir}</directory>
        <includes>
            <include>*.css</include>
        </includes>
        <targetPath>static</targetPath>
    </resource>

    <resource>
        <directory>${project.basedir}</directory>
        <includes>
            <include>*.js</include>
        </includes>
        <targetPath>static</targetPath>
    </resource>

</resources>
