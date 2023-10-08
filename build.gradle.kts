import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("java")
    id("maven-publish")
    signing
}
val env = System.getProperty("env", "DEV")
val isProd = "prod".contentEquals(env, ignoreCase = true)


val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMM")!!

/**
 * 生成版本号
 * @param mainVer 主版本号
 */
fun versionGen(mainVer: Any): Any {
    if (isProd) {
        return mainVer
    }

    val currentDate = dateTimeFormatter.format(LocalDateTime.now())
    return "${mainVer}.${currentDate}-SNAPSHOT"
}
group = "com.iwuyc.tools"
version = "2024.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}

tasks.register("__printlnTask") {
    println(System.getProperty("publishing.username", "none"))
    println(System.getProperty("env"))
    println(versionGen(version.toString()))
}

tasks {
    version = versionGen(version)
    withType<JavaCompile>().configureEach {
        options.encoding = "utf-8"
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        options.compilerArgs = listOf("-Xdoclint:none", "-Xlint:none", "-nowarn")
    }
    withType<Javadoc> {
        options {
            memberLevel = JavadocMemberLevel.PACKAGE
            // 添加javadoc的命令行参数
            val rootDir = layout.projectDirectory.asFile
            optionFiles = listOf(File(rootDir, "javadoc.options"))
        }

        // javadoc检查错误不中断后续流程
        isFailOnError = false
    }

    withType<Test>() {
        enabled = true
    }
}

tasks {
    withType<JavaCompile>() {
        options.encoding = "utf-8"
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }
    version = versionGen(version)
}

dependencies {
    annotationProcessor(libs.lombok)
    implementation(libs.bundles.commons.tools)

    compileOnly(libs.lombok)

    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testImplementation(libs.bundles.commons.tools)
    testImplementation(libs.bundles.test.tools)
}



publishing {

    publications {

        create<MavenPublication>("Tpg4jCoreJar") {
            beforeEvaluate {
                tasks.javadoc {

                }
            }

            from(components["java"])
            withBuildIdentifier()

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {


                packaging = "jar"
                name = "iwuyc-common-beans"

                description =
                    """Common beans tools.Include utility classes,and much much more."""

                url = "https://gitee.com/iwuyc-tools/iwuyc-common-beans"

                properties = mapOf(
                    "autoReleaseAfterClose" to "false"
                )

                licenses {
                    license {
                        name = "GNU Affero General Public License v3.0"
                        url = "https://gitee.com/iwuyc-tools/iwuyc-common-beans/blob/main/LICENSE"
                        distribution = "repo"
                    }
                }
                scm {
                    url = "http://i.iwuyc.com"
                    connection = "scm:git:https://gitee.com/iwuyc-tools/iwuyc-common-beans.git"
                    developerConnection = "scm:git:https://gitee.com/iwuyc-tools/iwuyc-common-beans.git"
                }
                developers {
                    developer {
                        email = "iwuyc@qq.com"
                        name = "Neil"
                        url = "http://i.iwuyc.com"
                        organizationUrl = "http://i.iwuyc.com"
                    }
                }

            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            isAllowInsecureProtocol = true
            val repoUrl = {
                if (isProd) "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                else "https://oss.sonatype.org/content/repositories/snapshots/"
            }
            url = uri(repoUrl)
            credentials {
                username = System.getProperty("publishing.username")
                password = System.getProperty("publishing.password")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(configurations.archives.get())
}

configurations.all {
    resolutionStrategy {
        // ./gradlew --refresh-dependencies  可以刷新依赖的缓存
        // SNAPSHOT dependencies
        cacheChangingModulesFor(1, TimeUnit.HOURS)
        // Release dependencies
        cacheDynamicVersionsFor(2, TimeUnit.HOURS)
    }
}
