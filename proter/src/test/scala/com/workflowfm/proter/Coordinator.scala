package com.workflowfm.proter
/*
    "interact correctly with a simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen: MockSimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 2L, 5)

      coordinator.addArrivalNow(5, Constant(3), gen)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L) // Last task finishes at 14L but the next arrival is still queued.
        ()
      }

    }

    "interact correctly with two simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 2L, 5)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 1L, 3L, 2L, 4)

      coordinator.addArrivalNow(5, Constant(3), gen1)
      coordinator.addArrival(1, 4, Constant(3), gen2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.foreach(_ should comply)
        gen2.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L)
        ()
      }
    }

    "interact correctly with two simulation generators using addArrivalNext" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 3L, 3L, 2L, 4)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 2L, 2L, 2L, 3)

      coordinator.addArrivalNext(4, Constant(3), gen1)
      coordinator.addArrivalNext(3, Constant(2), gen2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.foreach(_ should comply)
        gen2.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L)
        ()
      }
    }

    "interact correctly with an infinite simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen: MockSimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 1L, 5)

      coordinator.addInfiniteArrivalNow(Constant(3), gen)
      coordinator.limit(16L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen.sims.dropRight(1).foreach(_ should comply)
        coordinator.getTime() should be(16L)
        ()
      }
    }

    "interact correctly with two infinite simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 1L, 5)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 3L, 4L, 1L, 3)

      coordinator.addInfiniteArrivalNow(Constant(3), gen1)
      coordinator.addInfiniteArrival(3L, Constant(4), gen2)
      coordinator.limit(14L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.dropRight(1).foreach(_ should comply)
        gen2.sims.dropRight(1).foreach(_ should comply)
        coordinator.getTime() should be(14L)
        ()
      }
    }
  }
 */
