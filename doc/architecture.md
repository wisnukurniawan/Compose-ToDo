# Mercury architecture

Mercury is a chemical element, which usually used in traditional mining processes to find gold. I named this mercury to pretend the gold as "[Pure function](https://en.wikipedia.org/wiki/Pure_function)" as it intends to encourage maximal use of pure functions when using this. In the process, the more pure function we have, the more gold we have.

## Goal

Architecting this project subsequently achieved reliability and flexibility. Reliability means the programmer can understand how the program works with minimum effort. Flexibility means the project
is easy to change due to the nature of the project requirement in the future. Change is inevitable.

## Rationale

Programming, at its core, is about data processing (acquire, transform, store, search, manage, transmit data). Fundamentally data is immutable information. On mobile app, data processing are
dominantly consisted of how to process data into a set of UI elements. Mobile app need to handle multiple types of input with different velocities:

1. User gestures, such as tap on the screen. This kind of input is at positive velocity (which means that the data is pushed onto the application).
2. Server-side responses. This kind of input is at negative velocity (which means that the application needs to trigger something for the data to come).
3. Server-side pushes (such as push notifications, and streams). This kind of input is at positive velocity.

## Specification

Based on the above goal and rationale this architecture is abstracted using:

### Reactive streams: Kotlin flow, Swift combine

Reactive streams is designed to handle stream of data in an asynchronous system. Reactive streams combine the strengths of several programming paradigms, reactive programming and functional
programming. Reactive frameworks are possible to use, for android are RxJava and Kotlin flow, and for iOS are RxSwift and Swift combine. Kotlin flow and Swift combine is the chosen one because it is officially supported by Google and Apple.

### Immutability

All data properties must be treated as immutable by default. The key benefit of this is that immutable objects provide guarantees thread safe. Only sources or owners of data should be responsible for
updating the data they expose.

### Two main layers

The idea behind this separation is to make UI layer as lean as possible.

1. UI layer
2. Data layer

#### UI layer

Responsible to display data on the screen. UI here is just a passive component that react to any data changes. This leads to fewer inconsistencies and it makes the code easier to understand.

#### Data layer

Responsible to give correct data to the screen. Calling an API from data layer should be main-safe—safe from the main thread. The data layer must return flow/suspend. By doing this the data can be
composed easily.

### Sample Project

- https://github.com/wisnukurniawan/Compose-ToDo
- https://github.com/wisnukurniawan/Compose-Expense
- https://github.com/wisnukurniawan/analytics-debugview
- https://github.com/wisnukurniawan/siksorogo

P.S for more detailed implementation you can take a look https://github.com/wisnukurniawan/Compose-ToDo/blob/main/doc/project-structure.md or jump to code base
