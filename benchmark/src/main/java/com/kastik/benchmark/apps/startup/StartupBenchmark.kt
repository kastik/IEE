package com.kastik.benchmark.apps.startup

import androidx.benchmark.macro.BaselineProfileMode.Disable
import androidx.benchmark.macro.BaselineProfileMode.Require
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.StartupMode.COLD
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupWithoutPreCompilation() = startup(CompilationMode.None())

    @Test
    fun startupWithPartialCompilationAndDisabledBaselineProfile() = startup(
        CompilationMode.Partial(baselineProfileMode = Disable, warmupIterations = 1),
    )

    @Test
    fun startupPrecompiledWithBaselineProfile() =
        startup(CompilationMode.Partial(baselineProfileMode = Require))

    @Test
    fun startupFullyPrecompiled() = startup(CompilationMode.Full())

    @OptIn(ExperimentalMetricApi::class)
    private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.kastik.apps",
        metrics = listOf(
            StartupTimingMetric(),
            TraceSectionMetric("JIT Compiling %", label = "JIT compilation"),
            TraceSectionMetric("L%/%;", label = "ClassInit")
        ),
        compilationMode = compilationMode,
        iterations = 2,
        startupMode = COLD,
        setupBlock = {
            pressHome()
        },
    ) {
        startActivityAndWait()
    }
}