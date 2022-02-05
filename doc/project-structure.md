## Project structure

I separate the top level into 4 package

```
├── features
├── foundation
├── model
└── runtime
```

### Model

In this project there are three kind of model, UI model, domain model, data source model. These three models have different purpose.

#### Domain model

This is generic model that can be reused in all place. I encourage using model from this package to build a logic where feasible. This will be helpful in case we want to move it into multiplatform
world we only focus moving this model and it is usage.

#### Data source model

Each data source has it is own model, eg model from sqlite db, model from data store, model from server. So that whenever any changes not impact each other.

#### UI model

Used as UI state source of truth. It also to simplify the rendering operation. So that the complex part is not in the UI layer.

### Runtime

The top level of application. Used to initiate dependency. Used to register feature to make it accessible through navigation.

```
└── runtime
    └── navigation
```

### Feature

This is a place for our features. A feature is set of flow that created based on user requirements. A Feature Consist of 2 layers, UI and data. Data flow must be unidirectional, `UI` should render
after `Model` changes due to a `Input`.

```
└── features
   ├── dashboard
   ├── host
   ├── localized
   ├── login
   ├── logout
   ├── setting
   ├── splash
   ├── theme
   └── todo
       ├── all
       ├── detail
       ├── group
       ├── grouplist
       ├── groupmenu
       ├── main
       ├── scheduled
       ├── search
       ├── step
       └── taskreminder
```

### Foundation

Reusable component goes here.

- Reusable logic (pure functionality)
- Reusable UI component
- Consist of data source with no specific feature logic, only providing get and set operation

```
└── foundation
   ├── datasource
   ├── di
   ├── extension
   ├── localization
   ├── preview
   ├── theme
   ├── uicomponent
   ├── uiextension
   ├── viewmodel
   ├── window
   └── wrapper
```
