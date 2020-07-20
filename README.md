## Flows

### Setup and example code
```shell
$> sbt
sbt> publishLocal
sbt> project flowsExample
sbt> run
```
view generated timeline in `WorkflowFM-Simulator\flowsExample\output\FlowsMain.html`

### Usage
Define a `FlowSimulationActor` using the companion object by providing a flow which describes the order and method with which a sequence of tasks is executed.

A flow consists of tasks and opperators inspired by BPMN gateways. The currently supported opperators are:
- `NoTask` for a 'blank' task which does nothing
- `Just` for the special case where one might want a simulation consisting of a single task
- `Then` for sequence
- `And` for parallel
- `All` for multiple parallel tasks
- `Or` for inclusive or; run both tasks and continue once either one completes

You may also combine tasks with infix operators:
- `+` to "And" two tasks
- `>` to "Then" two tasks 
- `|` to "Or" two tasks together

e.g.: `(task1 + task2) > (task3 | task4)`  ==  `Then( And(task1,task2), Or(task3,task4) )` 

See the example for detailed usage.
