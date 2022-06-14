1. adb root
2. Change flavor to benchmark and release
4. Run the BaselineProfileGenerator and wait for its completion.
5. find -> com.example.app D/Benchmark: Usable output directory: /storage/emulated/0/Android/media/com.example.app
6. check -> ls /storage/emulated/0/Android/media/com.example.app
7. pull -> adb pull storage/emulated/0/Android/media/com.example.app/SampleStartupBenchmark_startup-baseline-prof.txt .
8. Rename the generated file to baseline-prof.txt and copy it to the src/main directory of your app module.
