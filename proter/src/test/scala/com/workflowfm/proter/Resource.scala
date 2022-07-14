package com.workflowfm.proter


trait ResourceTester extends TaskTester {
  final case class TestResourceMap(m: ResourceMap) {

    // pre-attach Tasks to resources
    def +(r: String, duration: Long): TestResourceMap = TestResourceMap(
      m.startTask(t(0L, Seq(r), Task.Medium, 0L, duration), 0L).getOrElse(m)
    )
  }

  object TestResourceMap {

    def apply(names: String*): TestResourceMap = TestResourceMap(
      ResourceMap(names.map(Resource(_, 1, 0)))
    )
  }
}
