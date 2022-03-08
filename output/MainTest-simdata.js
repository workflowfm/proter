var tasks = [
	"task1",
	"task2",
	"task3",
	"task4",
	"task5",
	"task6",
	"task7",
	"task8",
	"task9",
];

var resourceData = [
{label: "r1", times: [
	{"label":"task2 (sim)", task: "task2", "id":"a83e504a-3fa8-409c-81ed-c1ee665671f0", "priority": -1, "starting_time": 0, "ending_time": 6, delay: 0, cost: 0.0},
	{"label":"task1 (sim)", task: "task1", "id":"58415136-0df6-4d43-a5c0-c16b361ed7db", "priority": 0, "starting_time": 0, "ending_time": 6, delay: 0, cost: 0.0},
	{"label":"task4 (sim)", task: "task4", "id":"7e9599e4-389b-488e-a2c4-1511edf6c4e3", "priority": 0, "starting_time": 6, "ending_time": 8, delay: 6, cost: 0.0},
	{"label":"task7 (sim)", task: "task7", "id":"bb2049f1-d416-4a40-afb3-70a2ee2047d6", "priority": 0, "starting_time": 6, "ending_time": 7, delay: 6, cost: 0.0},
	{"label":"task6 (sim)", task: "task6", "id":"fed2cde4-3373-4991-ad9c-5d646e4493d5", "priority": 1, "starting_time": 8, "ending_time": 10, delay: 8, cost: 0.0},
	{"label":"task8 (sim)", task: "task8", "id":"792ec8a4-d77b-41f1-b0ad-d7a4f024d404", "priority": -1, "starting_time": 10, "ending_time": 11, delay: 10, cost: 0.0},
	{"label":"task3 (sim)", task: "task3", "id":"a401afd7-98fb-442a-86e8-4b45b879e674", "priority": -1, "starting_time": 11, "ending_time": 15, delay: 5, cost: 0.0},
	{"label":"task5 (sim)", task: "task5", "id":"d64c41cf-5e92-41d8-880c-e9abf5ba0d0a", "priority": 1, "starting_time": 15, "ending_time": 20, delay: 7, cost: 0.0},
	{"label":"task9 (sim)", task: "task9", "id":"5dab5814-3c11-4ce7-9c68-e14c5bf3f9ef", "priority": -1, "starting_time": 20, "ending_time": 25, delay: 9, cost: 0.0}
]},
{label: "r2", times: [
	{"label":"task2 (sim)", task: "task2", "id":"a83e504a-3fa8-409c-81ed-c1ee665671f0", "priority": -1, "starting_time": 0, "ending_time": 6, delay: 0, cost: 0.0},
	{"label":"task4 (sim)", task: "task4", "id":"7e9599e4-389b-488e-a2c4-1511edf6c4e3", "priority": 0, "starting_time": 6, "ending_time": 8, delay: 6, cost: 0.0},
	{"label":"task7 (sim)", task: "task7", "id":"bb2049f1-d416-4a40-afb3-70a2ee2047d6", "priority": 0, "starting_time": 6, "ending_time": 7, delay: 6, cost: 0.0},
	{"label":"task6 (sim)", task: "task6", "id":"fed2cde4-3373-4991-ad9c-5d646e4493d5", "priority": 1, "starting_time": 8, "ending_time": 10, delay: 8, cost: 0.0},
	{"label":"task8 (sim)", task: "task8", "id":"792ec8a4-d77b-41f1-b0ad-d7a4f024d404", "priority": -1, "starting_time": 10, "ending_time": 11, delay: 10, cost: 0.0},
	{"label":"task3 (sim)", task: "task3", "id":"a401afd7-98fb-442a-86e8-4b45b879e674", "priority": -1, "starting_time": 11, "ending_time": 15, delay: 5, cost: 0.0},
	{"label":"task9 (sim)", task: "task9", "id":"5dab5814-3c11-4ce7-9c68-e14c5bf3f9ef", "priority": -1, "starting_time": 20, "ending_time": 25, delay: 9, cost: 0.0}
]},
];

var simulationData = [
{label: "sim", times: [
	{"label":"task2 (sim)", task: "task2", "id":"a83e504a-3fa8-409c-81ed-c1ee665671f0", "priority": -1, "starting_time": 0, "ending_time": 6, delay: 0, cost: 0.0},
	{"label":"task1 (sim)", task: "task1", "id":"58415136-0df6-4d43-a5c0-c16b361ed7db", "priority": 0, "starting_time": 0, "ending_time": 6, delay: 0, cost: 0.0},
	{"label":"task4 (sim)", task: "task4", "id":"7e9599e4-389b-488e-a2c4-1511edf6c4e3", "priority": 0, "starting_time": 6, "ending_time": 8, delay: 6, cost: 0.0},
	{"label":"task7 (sim)", task: "task7", "id":"bb2049f1-d416-4a40-afb3-70a2ee2047d6", "priority": 0, "starting_time": 6, "ending_time": 7, delay: 6, cost: 0.0},
	{"label":"task6 (sim)", task: "task6", "id":"fed2cde4-3373-4991-ad9c-5d646e4493d5", "priority": 1, "starting_time": 8, "ending_time": 10, delay: 8, cost: 0.0},
	{"label":"task8 (sim)", task: "task8", "id":"792ec8a4-d77b-41f1-b0ad-d7a4f024d404", "priority": -1, "starting_time": 10, "ending_time": 11, delay: 10, cost: 0.0},
	{"label":"task3 (sim)", task: "task3", "id":"a401afd7-98fb-442a-86e8-4b45b879e674", "priority": -1, "starting_time": 11, "ending_time": 15, delay: 5, cost: 0.0},
	{"label":"task5 (sim)", task: "task5", "id":"d64c41cf-5e92-41d8-880c-e9abf5ba0d0a", "priority": 1, "starting_time": 15, "ending_time": 20, delay: 7, cost: 0.0},
	{"label":"task9 (sim)", task: "task9", "id":"5dab5814-3c11-4ce7-9c68-e14c5bf3f9ef", "priority": -1, "starting_time": 20, "ending_time": 25, delay: 9, cost: 0.0}
]},
];
