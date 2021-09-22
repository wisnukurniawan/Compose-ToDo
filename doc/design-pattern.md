# Architecture

<img src="/doc/arch-images/arch-diagram.png">

## Runtime

The Host of features

## Feature

A Feature Consist of 2 layers, UI and data. Data flow must be unidirectional, `UI` should render after `Model` changes due to a `Input`.

```
Input -> Model -> UI
```

Description:

- `Data`
  - Responsible to provide a correct data to UI
  - Follow repository pattern in case handle multiple source (server - local)
  - All public function must return coroutine flow or suspend
- `UI`
  - Responsible to render the data
  - Follow MVVM pattern
- `Model`
  - Pojo `kotlin data class`, can be a UI model/Data model/Domain model
  - `Model` property must be immutable `val` and for list as well avoid use `MutableList`
  - If possible all logic/calculation build by domain model
- `Input`
  - Can be user input/system event/server push/server pull

**P.S** Package structure by features

## Foundation

The infrastructure foundation, extracted reusable component goes here.

Characteristic:

- Consist of reusable logic, reusable UI component
- Consist of data source with no specific feature logic, only providing get and set
- Prefer have unit test
- Prefer pure function
