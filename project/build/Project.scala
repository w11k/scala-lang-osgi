import com.weiglewilczek.bnd4sbt.BNDPlugin
import com.weiglewilczek.bnd4sbt.ExecutionEnvironments._
import sbt.{ DefaultProject, ParentProject, ProjectInfo }
import sbt.Path._

/** scala-lang-osgi parent project */
class ScalaLangOsgiParentProject(info: ProjectInfo) extends ParentProject(info) {

  // Subprojects
  lazy val scalaLibraryProject = project("scala-library",  "scala-library",  new ScalaLibraryProject(_))

  /** scala-library subproject */
  class ScalaLibraryProject(info: ProjectInfo) extends DefaultProject(info) with BNDPlugin {

    override lazy val bndBundleSymbolicName = "com.weiglewilczek.scala-lang-osgi.scala-library"
    override lazy val bndBundleName         = "scala-library OSGi-fied"
    override lazy val bndBundleVersion      = buildScalaVersion

    override lazy val bndBundleRequiredExecutionEnvironment = Set(Java5, Java6)
    override lazy val bndBundleVendor                       = Some("Weigle Wilczek GmbH")
    override lazy val bndBundleLicense                      = Some("SCALA LICENSE (http://www.scala-lang.org/node/146)")

    override lazy val bndExportPackage        = Set("scala.*;version=%s".format(buildScalaVersion))
    override lazy val bndImportPackage        = Set("sun.*;resolution:=optional", "*")
    override lazy val bndDynamicImportPackage = Set("*")

    override lazy val bndVersionPolicy = Some("[$(@),$(@)]")
    override lazy val bndNoUses        = true

    override lazy val bndClasspath = fromFile(buildScalaInstance.libraryJar)
  }
}
