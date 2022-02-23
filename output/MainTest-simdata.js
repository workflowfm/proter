var tasks = [
	"A",
	"B",
	"C",
	"D",
];

var resourceData = [
{label: "r1", times: [
	{"label":"A (sim)", task: "A", "id":"f54f5448-a59d-4192-9f53-297781812720", "priority": 1, "starting_time": 0, "ending_time": 2, delay: 0, cost: 0.0},
	{"label":"C (sim)", task: "C", "id":"dec446b7-ba92-4ca4-a994-8a1b61e99e43", "priority": 0, "starting_time": 3, "ending_time": 5, delay: 3, cost: 0.0},
	{"label":"D (sim)", task: "D", "id":"4cf946f9-30f5-4dcf-97e1-1dce146eed20", "priority": -1, "starting_time": 5, "ending_time": 7, delay: 3, cost: 0.0}
]},
{label: "r2", times: [
	{"label":"B (sim)", task: "B", "id":"50b845b6-03a7-4ad4-ba97-64de0327c29c", "priority": 1, "starting_time": 0, "ending_time": 3, delay: 0, cost: 0.0},
	{"label":"C (sim)", task: "C", "id":"dec446b7-ba92-4ca4-a994-8a1b61e99e43", "priority": 0, "starting_time": 3, "ending_time": 5, delay: 3, cost: 0.0}
]},
];

var simulationData = [
{label: "sim", times: [
	{"label":"B (sim)", task: "B", "id":"50b845b6-03a7-4ad4-ba97-64de0327c29c", "priority": 1, "starting_time": 0, "ending_time": 3, delay: 0, cost: 0.0},
	{"label":"A (sim)", task: "A", "id":"f54f5448-a59d-4192-9f53-297781812720", "priority": 1, "starting_time": 0, "ending_time": 2, delay: 0, cost: 0.0},
	{"label":"C (sim)", task: "C", "id":"dec446b7-ba92-4ca4-a994-8a1b61e99e43", "priority": 0, "starting_time": 3, "ending_time": 5, delay: 3, cost: 0.0},
	{"label":"D (sim)", task: "D", "id":"4cf946f9-30f5-4dcf-97e1-1dce146eed20", "priority": -1, "starting_time": 5, "ending_time": 7, delay: 3, cost: 0.0}
]},
];
