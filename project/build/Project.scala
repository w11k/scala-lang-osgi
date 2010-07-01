import com.weiglewilczek.bnd4sbt._
import sbt._

/** scala-lang-osgi parent project */
class ScalaLangOsgiParentProject(info: ProjectInfo) extends ParentProject(info) {

  // Subprojects
  lazy val scalaLibraryProject  = project("scala-library",  "scala-library",  new ScalaLibraryProject(_))
  lazy val scalaCompilerProject = project("scala-compiler", "scala-compiler", new ScalaCompilerProject(_))

  // Publishing
  override def managedStyle = ManagedStyle.Maven
  override def deliverAction = super.deliverAction dependsOn(publishLocal) // Fix for issue 99!
  Credentials(Path.userHome / ".ivy2" / ".credentials" / ".scala-tools.org", log)
  lazy val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
//  lazy val publishTo = Resolver.file("Local Test Repository", Path fileProperty "java.io.tmpdir" asFile)

  /** scala-library subproject */
  class ScalaLibraryProject(info: ProjectInfo) extends ScalaLangOsgiBaseProject(info) {
    override lazy val bndExportPackage =
      "scala.*;version=%s".format(projectVersion.value) ::
      Nil
    override lazy val bndImportPackage =
      "sun.*;resolution:=optional" ::
      "*" ::
      Nil
    override lazy val bndDynamicImportPackage =
      "*" ::
      Nil
    override lazy val bndNoUses = true
    override lazy val bndClasspath = Path fromFile buildScalaInstance.libraryJar
    override lazy val bndOutput = super.bndOutput // Make it visible for ScalaCompilerProject!
  }

  /** scala-compiler subproject */
  class ScalaCompilerProject(info: ProjectInfo) extends ScalaLangOsgiBaseProject(info) {
    override lazy val bndExportPackage =
      "scala.tools.nsc.*;version=%s".format(projectVersion.value) ::
      Nil
    override lazy val bndImportPackage =
      "sun.*;resolution:=optional" ::
      "jline.*;resolution:=optional" ::
      "org.apache.tools.ant.*;resolution:=optional" ::
      "*" ::
      Nil
    override lazy val bndDynamicImportPackage =
      "*" ::
      Nil
    override lazy val bndRequireBundle =
      "%s;bundle-version=%s".format(scalaLibraryProject.bndBundleSymbolicName, projectVersion.value) ::
      Nil
    override lazy val bndClasspath = (Path fromFile buildScalaInstance.compilerJar) +++ scalaLibraryProject.bndOutput
  }
}

/** Base subproject with common settings for scala-lang-osgi. */
abstract class ScalaLangOsgiBaseProject(info: ProjectInfo) extends DefaultProject(info) with BNDPlugin {

  override lazy val moduleID = normalizedName // We don't need the crossScalaVersionString!
  override def managedStyle  = ManagedStyle.Maven

  override lazy val bndBundleSymbolicName   = normalizedName
  override lazy val bndBundleName           = "OSGi-fied %s" format bndBundleSymbolicName
  override lazy val bndExecutionEnvironment = Set(ExecutionEnvironments.Java5, ExecutionEnvironments.Java6)
  override lazy val bndBundleVendor         = Some("Weigle Wilczek GmbH")
  override lazy val bndBundleLicense        = Some("SCALA LICENSE (http://www.scala-lang.org/node/146)")
  override lazy val bndVersionPolicy        = Some("[$(@),$(@)]")
}
