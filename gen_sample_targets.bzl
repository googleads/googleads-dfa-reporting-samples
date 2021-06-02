"""Build extensions to simplify generating sample build/test targets."""

load("//tools/build_defs/build_test:build_test.bzl", "build_test")

COMMON_JAVA_SAMPLE_DEPS = [
    "//java/com/google/api/client/auth/oauth2",
    "//java/com/google/api/client/extensions/java6/auth/oauth2",
    "//java/com/google/api/client/extensions/jetty/auth/oauth2",
    "//java/com/google/api/client/googleapis/auth/oauth2",
    "//java/com/google/api/client/googleapis/util",
    "//java/com/google/api/client/http",
    "//java/com/google/api/client/json",
    "//java/com/google/api/client/util",
    "//java/com/google/api/client/util/store",
    "//java/com/google/common/base",
    "//java/com/google/common/collect",
    "//java/com/google/common/io",
]

def gen_java_targets(name):
    """Macro that defines build and test rules for all Java sample projects.

    Generates <name>-<version>-build and <name>-<version>-build-test targets.
    Ex: foo-v2.5-build, foo-v2.5-build-test

    Args:
      name: The prefix for all generated rules.
    """
    for pom_file in native.glob(["**/pom.xml"]):
        # Parse API version from the pom file path (ex: .../v2.5/pom.xml)
        version = pom_file.rsplit("/", 2)[0]

        versioned_rule_name = "%s-%s-build" % (name, version)

        library_args = {
            "name": versioned_rule_name,
            "srcs": native.glob(["%s/**/*.java" % version]),
            "deps": COMMON_JAVA_SAMPLE_DEPS + [
                "//apiserving/discoverydata/dfareporting:dfareporting-%s" % version,
            ],
        }

        native.java_library(**library_args)
        build_test(
            name = "%s-test" % versioned_rule_name,
            targets = [":%s" % versioned_rule_name],
        )
