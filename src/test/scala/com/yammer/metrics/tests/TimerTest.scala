package com.yammer.metrics.tests


import org.scalatest.matchers.MustMatchers
import org.scalatest.Spec
import com.yammer.metrics.Timer
import com.yammer.time.Duration

class TimerTest extends Spec with MustMatchers {
  val precision = 5.0 // milliseconds

  describe("timing an event") {
    it("returns the event's value") {
      val timer = new Timer
      timer.time { 1 + 1 } must equal(2)
    }

    it("records the duration of the event") {
      val timer = new Timer
      timer.time { Thread.sleep(10) }
      timer.mean.ms.value must be(10.0 plusOrMinus precision)
    }

    it("records the existence of the event") {
      val timer = new Timer
      timer.time { Thread.sleep(10) }

      timer.count must be(1)
    }
  }

  describe("a blank timer") {
    val timer = new Timer

    it("has a max of zero") {
      timer.max.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a min of zero") {
      timer.min.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a mean of zero") {
      timer.mean.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a standard deviation of zero") {
      timer.standardDeviation.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a median of zero") {
      timer.median.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a 95th percentile of zero") {
      timer.p95.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a 98th percentile of zero") {
      timer.p98.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a 99th percentile of zero") {
      timer.p99.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a 99.9th percentile of zero") {
      timer.p999.ms.value must be(0.0 plusOrMinus precision)
    }

    it("has a count of zero") {
      timer.count must be (0)
    }
  }

  describe("timing a series of events") {
    val timer = new Timer
    timer ++= List(
      Duration.milliseconds(10),
      Duration.milliseconds(20),
      Duration.milliseconds(20),
      Duration.milliseconds(30),
      Duration.milliseconds(40)
    )

    it("calculates the maximum duration") {
      timer.max.ms.value must be(40.0 plusOrMinus precision)
    }

    it("calculates the minimum duration") {
      timer.min.ms.value must be(10.0 plusOrMinus precision)
    }

    it("calculates the mean") {
      timer.mean.ms.value must be(24.0 plusOrMinus precision)
    }

    it("calculates the standard deviation") {
      timer.standardDeviation.ms.value must be(11.4 plusOrMinus precision)
    }

    it("calculates the median") {
      timer.median.ms.value must be(20.0 plusOrMinus precision)
    }

    it("calculates the 95th percentile") {
      timer.p95.ms.value must be(40.0 plusOrMinus precision)
    }

    it("calculates the 98th percentile") {
      timer.p98.ms.value must be(40.0 plusOrMinus precision)
    }

    it("calculates the 99th percentile") {
      timer.p99.ms.value must be(40.0 plusOrMinus precision)
    }

    it("calculates the 99.9th percentile") {
      timer.p999.ms.value must be(40.0 plusOrMinus precision)
    }

    it("records the count") {
      timer.count must be (5)
    }
  }

  describe("timing crazy-variant values") {
    val timer = new Timer
    timer ++= List(
      Duration.milliseconds(Long.MaxValue),
      Duration.milliseconds(0)
    )

    it("calculates the standard deviation without overflowing") {
      timer.standardDeviation.ms.value must be(6.521908912666392E12 plusOrMinus 1E3)
    }
  }
}
