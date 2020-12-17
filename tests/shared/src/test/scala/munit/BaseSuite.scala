package munit

import munit.internal.PlatformCompat

class BaseSuite extends FunSuite {

  override def munitTestTransforms: List[TestTransform] =
    super.munitTestTransforms ++ List(
      new TestTransform(
        "BaseSuite",
        { test =>
          def isDotty: Boolean =
            !BuildInfo.scalaVersion.startsWith("2.")
          def is213: Boolean =
            BuildInfo.scalaVersion.startsWith("2.13") || isDotty
          if (test.tags(NoDotty) && isDotty) {
            test.tag(Ignore)
          } else if (
            test.tags(OnlyFromM3) && "3.0.0-M2" == BuildInfo.scalaVersion
          ) {
            test.tag(Ignore)
          } else if (test.tags(Only213) && !is213) {
            test.tag(Ignore)
          } else if (test.tags(OnlyJVM) && !PlatformCompat.isJVM) {
            test.tag(Ignore)
          } else {
            test
          }
        }
      )
    )
}
