import com.weiglewilczek.bnd4sbt._
import sbt._

/** scala-lang-osgi parent project */
class ScalaLangOsgiParentProject(info: ProjectInfo) extends ParentProject(info) {

  // Subprojects
  lazy val scalaLibraryProject = project("scala-library",  "scala-library",  new ScalaLibraryProject(_))

  // Publishing
  override def managedStyle = ManagedStyle.Maven
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
  Credentials(Path.userHome / ".ivy2" / ".credentials" / ".scala-tools.org", log)

  // Fix for issue 99
  // override def deliverAction = super.publishAction dependsOn(publishLocal)

  /** scala-library subproject */
  class ScalaLibraryProject(info: ProjectInfo) extends DefaultProject(info) with BNDPlugin {

    override lazy val bndBundleSymbolicName   = "com.weiglewilczek.scala-lang-osgi.scala-library"
    override lazy val bndBundleName           = "scala-library OSGi-fied"
    override lazy val bndBundleVersion        = buildScalaVersion
    override lazy val bndExecutionEnvironment = Set(ExecutionEnvironments.Java5, ExecutionEnvironments.Java6)
    override lazy val bndBundleVendor         = Some("Weigle Wilczek GmbH")
    override lazy val bndBundleLicense        = Some("SCALA LICENSE (http://www.scala-lang.org/node/146)")
    override lazy val bndExportPackage        = Set("scala.*;version=%s".format(buildScalaVersion))
    override lazy val bndImportPackage        = Set("sun.*;resolution:=optional", "*")
    override lazy val bndDynamicImportPackage = Set("*")
    override lazy val bndVersionPolicy        = Some("[$(@),$(@)]")
    override lazy val bndNoUses               = true
    override lazy val bndClasspath            = Path fromFile buildScalaInstance.libraryJar

    override def managedStyle = ManagedStyle.Maven
  }
}
