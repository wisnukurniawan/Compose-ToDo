# Compose side effect

### Basic state

```kotlin
val mutableState = remember { mutableStateOf(default) }
// or
var value by remember { mutableStateOf(default) }
// or
val (value, setValue) = remember { mutableStateOf(default) }
```

### Non suspended

1. DisposableEffect, effects that require cleanup

```kotlin
fun DisposableEffect(
    key1: Any?,
    effect: DisposableEffectScope.() -> DisposableEffectResult
) {
    remember(key1) { DisposableEffectImpl(effect) }
}
```

2. SideEffect, publish Compose state to non-compose code

```kotlin
fun SideEffect(
    effect: () -> Unit
) {
    currentComposer.recordSideEffect(effect)
}
```

### Suspended

1. LaunchedEffect: run suspend functions in the scope of a composable. e.g collectAsState, collectAsState actually suspended side effect

```kotlin
fun LaunchedEffect(
    key1: Any?,
    block: suspend CoroutineScope.() -> Unit
) {
    val applyContext = currentComposer.applyCoroutineContext
    remember(key1) { LaunchedEffectImpl(applyContext, block) }
}
```

rememberUpdatedState: reference a value in an effect that shouldn't restart if the value changes. e.g click callback

one time effect `LaunchedEffect(true) { ... }`, but that is not enough, If onTimeout changes while the side-effect is in progress, there's no guarantee that the last onTimeout is called when the
effect finishes.

LaunchedEffect restarts di setiap ada perubahan di key. Mungkin, ketika kita taruh sebuah CALLBACK di dalamnya, DAN kita ga mau CALLBACK itu ke restart ketika LaunchedEffect ke restart

It wraps the parameter with remembered MutableState and updates its value on every recomposition. By doing this, the LaunchedEffect is referencing the same instance of the MutableState object instead
of the text parameter. So even after recomposition, the LaunchedEffect still references the correct instance, which changes only its internal value.

```kotlin
fun <T> rememberUpdatedState(newValue: T): State<T> = remember { mutableStateOf(newValue) }
    .apply { value = newValue }
```

2. rememberCoroutineScope: obtain a composition-aware scope to launch a coroutine outside a composable

```kotlin
inline fun rememberCoroutineScope(
    getContext: @DisallowComposableCalls () -> CoroutineContext = { EmptyCoroutineContext }
): CoroutineScope {
    val composer = currentComposer
    val wrapper = remember {
        CompositionScopedCoroutineScopeCanceller(
            createCompositionCoroutineScope(getContext(), composer)
        )
    }
    return wrapper.coroutineScope
}
```

3. produceState build by LaunchedEffect, convert non-Compose state into Compose state

```kotlin
fun <T> produceState(
    initialValue: T,
    key1: Any?,
    @BuilderInference producer: suspend ProduceStateScope<T>.() -> Unit
): State<T> {
    val result = remember { mutableStateOf(initialValue) }
    LaunchedEffect(key1) {
        ProduceStateScopeImpl(result, coroutineContext).producer()
    }
    return result
}
```

4. derivedStateOf: convert one or multiple state objects into another state

```kotlin
fun <T> derivedStateOf(calculation: () -> T): State<T> = DerivedSnapshotState(calculation)
```

5. snapshotFlow: convert Compose's State into Flows
