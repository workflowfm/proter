package com.workflowfm.proter
package flows

import flows.given
import cases.Case
import schedule.GreedyScheduler
import state.Simulation

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.LoneElement

import cats.{ Monad, MonadError }
import cats.implicits.*
import cats.data.StateT
import cats.effect.IO
import cats.effect.implicits.*
import cats.effect.kernel.Ref
import cats.effect.std.{ Random, UUIDGen }
import cats.effect.testing.scalatest.AsyncIOSpec

class FlowTests extends AsyncWordSpec with AsyncIOSpec with Matchers with LoneElement {

  "Flows" should {
    "execute no tasks" in {
      val flow = NoTask
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random       

        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1 `should` be (init)
      } yield ()
    }

    "execute a single flow" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random       
        gen = summon[UUIDGen[IO]]

        t1id <- gen.randomUUID
        task = Task("OneTask", 2L)
        .withPriority(10)
        .withCost(6)
        .withResources(Seq("R"))
        .withID(t1id)
        ti1 <- task.create[IO]("Case", 0L)

        flow = FlowTask(task)

        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)

        ref1 = ret1.cases.get("Case").getOrElse(ref)

        sim2 = ret1.copy(time = ti1.duration)
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2
        _ = ret2 `should` be (sim2.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }


    "execute an AND of two tasks finishing at different times" in {     
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        // add resources so that they don't start immediately!
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 2L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 0L)    

        flow = And(task1, task2)

        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks `should` have size (2)
        _ = ret1.tasks `should` contain (ti1)
        _ = ret1.tasks `should` contain (ti2)

        ref1 = ret1.cases.get("Case").getOrElse(ref)

        sim2 = ret1.copy(time = ti1.duration)
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2
        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ti2.duration)
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3 `should` be (sim3.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()

    }

    "execute an AND of two tasks finishing at the same time" in {     
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        // add resources so that they don't start immediately!
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 1L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 0L)    

        flow = And(task1, task2)

        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks `should` have size (2)
        _ = ret1.tasks `should` contain (ti1)
        _ = ret1.tasks `should` contain (ti2)

        ref1 = ret1.cases.get("Case").getOrElse(ref)

        sim2 = ret1.copy(time = ti1.duration)
        s2 <- ref1.completed(ti1.duration, Seq(ti1, ti2))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2 `should` be (sim2.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()

    }

    "execute a THEN of two tasks" in {     
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        // add resources so that they don't start immediately!
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 2L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 1L)    

        flow = Then(task1, task2)

        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)
        
        ref1 = ret1.cases.get("Case").getOrElse(ref)

        sim2 = ret1.copy(time = ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2.tasks.loneElement `should` be (ti2)

        ref2 = ret2.cases.get("Case").getOrElse(ref)

        sim3 = ret2.copy(time = ti2.duration)
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3 `should` be (sim3.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()

    }

    "execute nested ANDs (left associativity)" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 1L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 0L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 1L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 0L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 2L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 0L)    
        t5id <- gen.randomUUID
        task5 = FlowTask(Task("task5", 2L).withResources(Seq("R")).withID(t5id))
        ti5 <- task5.task.create[IO]("Case", 0L)    

        flow = And(And(And(And(task1, task2), task3), task4), task5)


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks `should` have size (5)
        _ = ret1.tasks `should` contain (ti1)
        _ = ret1.tasks `should` contain (ti2)
        _ = ret1.tasks `should` contain (ti3)
        _ = ret1.tasks `should` contain (ti4)
        _ = ret1.tasks `should` contain (ti5)

        ref1 = ret1.cases.get("Case").getOrElse(ref)

        sim2 = ret1.copy(time = ti1.duration)
        s2 <- ref1.completed(ti1.duration, Seq(ti1, ti2, ti3))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2
        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ti4.duration)
        s3 <- ref2.completed(ti2.duration, Seq(ti4, ti5))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3 `should` be (sim3.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute nested ANDs (right associativity)" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 1L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 0L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 1L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 0L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 2L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 0L)    
        t5id <- gen.randomUUID
        task5 = FlowTask(Task("task5", 2L).withResources(Seq("R")).withID(t5id))
        ti5 <- task5.task.create[IO]("Case", 0L)    

        flow = And(task1, And(task2, And(task3, And(task4, task5))))


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks `should` have size (5)
        _ = ret1.tasks `should` contain (ti1)
        _ = ret1.tasks `should` contain (ti2)
        _ = ret1.tasks `should` contain (ti3)
        _ = ret1.tasks `should` contain (ti4)
        _ = ret1.tasks `should` contain (ti5)

        ref1 = ret1.cases.get("Case").getOrElse(ref)

        sim2 = ret1.copy(time = ti1.duration)
        s2 <- ref1.completed(ti1.duration, Seq(ti1, ti2, ti3))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2
        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ti4.duration)
        s3 <- ref2.completed(ti2.duration, Seq(ti4, ti5))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3 `should` be (sim3.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute nested THENs (left associativity)" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 1L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 1L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 1L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 2L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 1L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 3L)    
        t5id <- gen.randomUUID
        task5 = FlowTask(Task("task5", 1L).withResources(Seq("R")).withID(t5id))
        ti5 <- task5.task.create[IO]("Case", 4L)    

        flow = Then(Then(Then(Then(task1, task2), task3), task4), task5)


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ret1.time + ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2.tasks.loneElement `should` be (ti2)

        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ret2.time + ti2.duration, tasks = Set())
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3.tasks.loneElement `should` be (ti3)

        ref3 = ret3.cases.get("Case").getOrElse(ref)
        sim4 = ret3.copy(time = ret3.time + ti3.duration, tasks = Set())
        s4 <- ref3.completed(ti3.duration, Seq(ti3))
        r4 <- s4.run(sim4)
        (ret4, e4) = r4

        _ = ret4.tasks.loneElement `should` be (ti4)

        ref4 = ret4.cases.get("Case").getOrElse(ref)
        sim5 = ret4.copy(time = ret4.time + ti4.duration, tasks = Set())
        s5 <- ref4.completed(ti4.duration, Seq(ti4))
        r5 <- s5.run(sim5)
        (ret5, e5) = r5

        _ = ret5.tasks.loneElement `should` be (ti5)

        ref5 = ret5.cases.get("Case").getOrElse(ref)
        sim6 = ret5.copy(time = ret5.time + ti5.duration, tasks = Set())
        s6 <- ref5.completed(ti5.duration, Seq(ti5))
        r6 <- s6.run(sim6)
        (ret6, e6) = r6

        _ = ret6 `should` be (sim6.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute nested THENs (right associativity)" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 1L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 1L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 1L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 2L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 1L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 3L)    
        t5id <- gen.randomUUID
        task5 = FlowTask(Task("task5", 1L).withResources(Seq("R")).withID(t5id))
        ti5 <- task5.task.create[IO]("Case", 4L)    

        flow = Then(task1, Then(task2, Then(task3, Then(task4, task5))))


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ret1.time + ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2.tasks.loneElement `should` be (ti2)

        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ret2.time + ti2.duration, tasks = Set())
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3.tasks.loneElement `should` be (ti3)

        ref3 = ret3.cases.get("Case").getOrElse(ref)
        sim4 = ret3.copy(time = ret3.time + ti3.duration, tasks = Set())
        s4 <- ref3.completed(ti3.duration, Seq(ti3))
        r4 <- s4.run(sim4)
        (ret4, e4) = r4

        _ = ret4.tasks.loneElement `should` be (ti4)

        ref4 = ret4.cases.get("Case").getOrElse(ref)
        sim5 = ret4.copy(time = ret4.time + ti4.duration, tasks = Set())
        s5 <- ref4.completed(ti4.duration, Seq(ti4))
        r5 <- s5.run(sim5)
        (ret5, e5) = r5

        _ = ret5.tasks.loneElement `should` be (ti5)

        ref5 = ret5.cases.get("Case").getOrElse(ref)
        sim6 = ret5.copy(time = ret5.time + ti5.duration, tasks = Set())
        s6 <- ref5.completed(ti5.duration, Seq(ti5))
        r6 <- s6.run(sim6)
        (ret6, e6) = r6

        _ = ret6 `should` be (sim6.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute nested THENs (operator)" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 1L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 1L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 1L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 2L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 1L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 3L)    
        t5id <- gen.randomUUID
        task5 = FlowTask(Task("task5", 1L).withResources(Seq("R")).withID(t5id))
        ti5 <- task5.task.create[IO]("Case", 4L)    

        flow = task1 > task2 > task3 > task4 > task5


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ret1.time + ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2.tasks.loneElement `should` be (ti2)

        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ret2.time + ti2.duration, tasks = Set())
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3.tasks.loneElement `should` be (ti3)

        ref3 = ret3.cases.get("Case").getOrElse(ref)
        sim4 = ret3.copy(time = ret3.time + ti3.duration, tasks = Set())
        s4 <- ref3.completed(ti3.duration, Seq(ti3))
        r4 <- s4.run(sim4)
        (ret4, e4) = r4

        _ = ret4.tasks.loneElement `should` be (ti4)

        ref4 = ret4.cases.get("Case").getOrElse(ref)
        sim5 = ret4.copy(time = ret4.time + ti4.duration, tasks = Set())
        s5 <- ref4.completed(ti4.duration, Seq(ti4))
        r5 <- s5.run(sim5)
        (ret5, e5) = r5

        _ = ret5.tasks.loneElement `should` be (ti5)

        ref5 = ret5.cases.get("Case").getOrElse(ref)
        sim6 = ret5.copy(time = ret5.time + ti5.duration, tasks = Set())
        s6 <- ref5.completed(ti5.duration, Seq(ti5))
        r6 <- s6.run(sim6)
        (ret6, e6) = r6

        _ = ret6 `should` be (sim6.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute a task THEN NoTask" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)

        flow = task1 > NoTask


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ret1.time + ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2 `should` be (sim2.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute NoTask THEN a task" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)

        flow = NoTask > task1 


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks.loneElement `should` be (ti1)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ret1.time + ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2 `should` be (sim2.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }


    "execute an AND of THENs" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 2L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 1L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 2L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 0L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 1L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 2L)    

        flow = And(Then(task1, task2), Then(task3, task4))


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks `should` have size (2)
        _ = ret1.tasks `should` contain (ti1)
        _ = ret1.tasks `should` contain (ti3)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2.tasks.loneElement `should` be (ti2)

        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ti3.duration, tasks = Set())
        s3 <- ref2.completed(ti3.duration, Seq(ti3))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3.tasks.loneElement `should` be (ti4)

        ref3 = ret3.cases.get("Case").getOrElse(ref)
        sim4 = ret3.copy(time = 4L, tasks = Set())
        s4 <- ref3.completed(4L, Seq(ti2, ti4))
        r4 <- s4.run(sim4)
        (ret4, e4) = r4

        _ = ret4 `should` be (sim4.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

    "execute a THEN of ANDs" in {
      val init = Simulation[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random     
        gen = summon[UUIDGen[IO]]  

        t1id <- gen.randomUUID
        task1 = FlowTask(Task("task1", 1L).withResources(Seq("R")).withID(t1id))
        ti1 <- task1.task.create[IO]("Case", 0L)
        t2id <- gen.randomUUID
        task2 = FlowTask(Task("task2", 2L).withResources(Seq("R")).withID(t2id))
        ti2 <- task2.task.create[IO]("Case", 0L)    
        t3id <- gen.randomUUID
        task3 = FlowTask(Task("task3", 1L).withResources(Seq("R")).withID(t3id))
        ti3 <- task3.task.create[IO]("Case", 2L)    
        t4id <- gen.randomUUID
        task4 = FlowTask(Task("task4", 1L).withResources(Seq("R")).withID(t4id))
        ti4 <- task4.task.create[IO]("Case", 2L)    

        flow = Then(And(task1, task2), And(task3, task4))


        ref <- summon[Case[IO, Flow]].init("Case", 0, 0, flow)

        s1 <- ref.run()
        r1 <- s1.run(init)
        (ret1, e1) = r1

        _ = ret1.tasks `should` have size (2)
        _ = ret1.tasks `should` contain (ti1)
        _ = ret1.tasks `should` contain (ti2)

        ref1 = ret1.cases.get("Case").getOrElse(ref)
        sim2 = ret1.copy(time = ti1.duration, tasks = Set())
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2

        _ = ret2.tasks `shouldBe` empty

        ref2 = ret2.cases.get("Case").getOrElse(ref)
        sim3 = ret2.copy(time = ti2.duration, tasks = Set())
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3

        _ = ret3.tasks `should` have size (2)
        _ = ret3.tasks `should` contain (ti3)
        _ = ret3.tasks `should` contain (ti4)

        ref3 = ret3.cases.get("Case").getOrElse(ref)
        sim4 = ret3.copy(time = 4L, tasks = Set())
        s4 <- ref3.completed(4L, Seq(ti3, ti4))
        r4 <- s4.run(sim4)
        (ret4, e4) = r4

        _ = ret4 `should` be (sim4.copy(
          waiting = Map(),
          cases = Map()
        ))
      } yield ()
    }

  }
}

