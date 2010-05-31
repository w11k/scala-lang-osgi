import com.weiglewilczek.bnd4sbt.BNDPlugin
import sbt._

class ScalaLangOsgiParentProject(info: ProjectInfo) extends ParentProject(info) {

  // Subprojects
  lazy val scalaLibraryProject = project("scala-library",  "scala-library",  new ScalaLibraryProject(_))
  class ScalaLibraryProject(info: ProjectInfo) extends DefaultProject(info) with BNDPlugin {
    override lazy val bndBundleSymbolicName = "com.weiglewilczek.scala-lang-osgi.scala-library"
    override lazy val bndExportPackage = Set("scala.*;version=%s".format(buildScalaVersion))
    override lazy val bndImportPackage = Set("sun.*;resolution:=optional", "*")
    override lazy val bndFileName = "%s_%s.jar".format(project.name, buildScalaVersion)
    override lazy val bndClasspath = Path fromFile buildScalaInstance.libraryJar
  }
}
