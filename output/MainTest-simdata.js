var tasks = [
	"task1",
	"task2",
	"task3",
];

var resourceData = [
{label: "r1", times: [
	{"label":"task1 (flow0)", task: "task1", "id":"c818438e-4653-4143-b7d9-f4aad005a509", "starting_time": 0, "ending_time": 2, delay: 0, cost: 0.0},
	{"label":"task1 (flow1)", task: "task1", "id":"8caf862e-cb3b-4444-993a-e5e62d808d66", "starting_time": 10, "ending_time": 12, delay: 0, cost: 0.0},
	{"label":"task1 (flow2)", task: "task1", "id":"a327efd1-b8d8-4ded-ac04-49b030ba2a7f", "starting_time": 20, "ending_time": 22, delay: 0, cost: 0.0},
	{"label":"task1 (flow3)", task: "task1", "id":"e6f531b6-8c26-4a69-ab6c-b67ba1ba9fd0", "starting_time": 30, "ending_time": 32, delay: 0, cost: 0.0},
	{"label":"task1 (flow4)", task: "task1", "id":"2286f6f5-3b4a-49ed-b7dd-88c2f95826cb", "starting_time": 40, "ending_time": 42, delay: 0, cost: 0.0},
	{"label":"task1 (flow5)", task: "task1", "id":"1086bc8e-947f-497e-b476-33e880237c08", "starting_time": 50, "ending_time": 52, delay: 0, cost: 0.0},
	{"label":"task1 (flow6)", task: "task1", "id":"9d31bf5f-7b3f-4277-a632-95e837d02df1", "starting_time": 60, "ending_time": 62, delay: 0, cost: 0.0},
	{"label":"task1 (flow7)", task: "task1", "id":"b3695c13-f422-4c1e-b103-a08a6ad1e8dd", "starting_time": 70, "ending_time": 72, delay: 0, cost: 0.0},
	{"label":"task1 (flow8)", task: "task1", "id":"23e6905b-f79c-4999-94eb-72382bd9e47e", "starting_time": 80, "ending_time": 82, delay: 0, cost: 0.0},
	{"label":"task1 (flow9)", task: "task1", "id":"c9797aa8-d4e0-4485-943e-39cf84a3281b", "starting_time": 90, "ending_time": 92, delay: 0, cost: 0.0}
]},
{label: "r2", times: [
	{"label":"task3 (flow0)", task: "task3", "id":"9ce52424-ea67-494a-9fbf-72cdc61bb0e0", "starting_time": 5, "ending_time": 9, delay: 0, cost: 0.0},
	{"label":"task3 (flow1)", task: "task3", "id":"36a1c133-7041-412e-a923-940bae592689", "starting_time": 15, "ending_time": 19, delay: 0, cost: 0.0},
	{"label":"task3 (flow2)", task: "task3", "id":"e2f4e395-b447-4294-8c12-f315eec04960", "starting_time": 25, "ending_time": 29, delay: 0, cost: 0.0},
	{"label":"task3 (flow3)", task: "task3", "id":"8889c9a5-c947-4633-aac3-c58b3f39fd1a", "starting_time": 35, "ending_time": 39, delay: 0, cost: 0.0},
	{"label":"task3 (flow4)", task: "task3", "id":"d86f136b-6b7b-418e-a9de-02c2089ebe6e", "starting_time": 45, "ending_time": 49, delay: 0, cost: 0.0},
	{"label":"task3 (flow5)", task: "task3", "id":"4fd12fc7-9878-4ac3-b1f5-123ae61567bc", "starting_time": 55, "ending_time": 59, delay: 0, cost: 0.0},
	{"label":"task3 (flow6)", task: "task3", "id":"740b879b-f032-4b17-b050-67fcead37e24", "starting_time": 65, "ending_time": 69, delay: 0, cost: 0.0},
	{"label":"task3 (flow7)", task: "task3", "id":"bad8261e-24e1-435e-8de5-58e2899c5f76", "starting_time": 75, "ending_time": 79, delay: 0, cost: 0.0},
	{"label":"task3 (flow8)", task: "task3", "id":"b26f33fa-8ab1-4f39-abaf-bf4258dd86c4", "starting_time": 85, "ending_time": 89, delay: 0, cost: 0.0},
	{"label":"task3 (flow9)", task: "task3", "id":"0976d314-5274-49f5-977a-bba9248038e5", "starting_time": 95, "ending_time": 99, delay: 0, cost: 0.0}
]},
{label: "r3", times: [
	{"label":"task2 (flow0)", task: "task2", "id":"7eb0ed6f-2a0f-4f0a-b5b2-57be158c5111", "starting_time": 2, "ending_time": 5, delay: 0, cost: 0.0},
	{"label":"task2 (flow1)", task: "task2", "id":"2b557247-473b-42b4-b3d7-3dfd968c5e71", "starting_time": 12, "ending_time": 15, delay: 0, cost: 0.0},
	{"label":"task2 (flow2)", task: "task2", "id":"310a2329-b428-4d3b-808e-3d235bf9d35e", "starting_time": 22, "ending_time": 25, delay: 0, cost: 0.0},
	{"label":"task2 (flow3)", task: "task2", "id":"f24a3ec8-df26-4717-b516-9c4e970e2b60", "starting_time": 32, "ending_time": 35, delay: 0, cost: 0.0},
	{"label":"task2 (flow4)", task: "task2", "id":"1348dba8-2c44-44b2-b525-3d6fca9901e7", "starting_time": 42, "ending_time": 45, delay: 0, cost: 0.0},
	{"label":"task2 (flow5)", task: "task2", "id":"0dffb8d1-ec75-46f5-b6c1-2296f078546e", "starting_time": 52, "ending_time": 55, delay: 0, cost: 0.0},
	{"label":"task2 (flow6)", task: "task2", "id":"50366cf2-8105-41cd-9c8b-d184ea876a15", "starting_time": 62, "ending_time": 65, delay: 0, cost: 0.0},
	{"label":"task2 (flow7)", task: "task2", "id":"1aa1cf15-1e6d-4ecc-acf6-b016c353fcc7", "starting_time": 72, "ending_time": 75, delay: 0, cost: 0.0},
	{"label":"task2 (flow8)", task: "task2", "id":"514ad6dd-a2ea-4015-a130-05e81bb49bc1", "starting_time": 82, "ending_time": 85, delay: 0, cost: 0.0},
	{"label":"task2 (flow9)", task: "task2", "id":"7b9c9283-45d3-473f-80bb-81d12d19bc0f", "starting_time": 92, "ending_time": 95, delay: 0, cost: 0.0}
]},
];

var simulationData = [
{label: "flow0", times: [
	{"label":"task1 (flow0)", task: "task1", "id":"c818438e-4653-4143-b7d9-f4aad005a509", "starting_time": 0, "ending_time": 2, delay: 0, cost: 0.0},
	{"label":"task2 (flow0)", task: "task2", "id":"7eb0ed6f-2a0f-4f0a-b5b2-57be158c5111", "starting_time": 2, "ending_time": 5, delay: 0, cost: 0.0},
	{"label":"task3 (flow0)", task: "task3", "id":"9ce52424-ea67-494a-9fbf-72cdc61bb0e0", "starting_time": 5, "ending_time": 9, delay: 0, cost: 0.0}
]},
{label: "flow1", times: [
	{"label":"task1 (flow1)", task: "task1", "id":"8caf862e-cb3b-4444-993a-e5e62d808d66", "starting_time": 10, "ending_time": 12, delay: 0, cost: 0.0},
	{"label":"task2 (flow1)", task: "task2", "id":"2b557247-473b-42b4-b3d7-3dfd968c5e71", "starting_time": 12, "ending_time": 15, delay: 0, cost: 0.0},
	{"label":"task3 (flow1)", task: "task3", "id":"36a1c133-7041-412e-a923-940bae592689", "starting_time": 15, "ending_time": 19, delay: 0, cost: 0.0}
]},
{label: "flow2", times: [
	{"label":"task1 (flow2)", task: "task1", "id":"a327efd1-b8d8-4ded-ac04-49b030ba2a7f", "starting_time": 20, "ending_time": 22, delay: 0, cost: 0.0},
	{"label":"task2 (flow2)", task: "task2", "id":"310a2329-b428-4d3b-808e-3d235bf9d35e", "starting_time": 22, "ending_time": 25, delay: 0, cost: 0.0},
	{"label":"task3 (flow2)", task: "task3", "id":"e2f4e395-b447-4294-8c12-f315eec04960", "starting_time": 25, "ending_time": 29, delay: 0, cost: 0.0}
]},
{label: "flow3", times: [
	{"label":"task1 (flow3)", task: "task1", "id":"e6f531b6-8c26-4a69-ab6c-b67ba1ba9fd0", "starting_time": 30, "ending_time": 32, delay: 0, cost: 0.0},
	{"label":"task2 (flow3)", task: "task2", "id":"f24a3ec8-df26-4717-b516-9c4e970e2b60", "starting_time": 32, "ending_time": 35, delay: 0, cost: 0.0},
	{"label":"task3 (flow3)", task: "task3", "id":"8889c9a5-c947-4633-aac3-c58b3f39fd1a", "starting_time": 35, "ending_time": 39, delay: 0, cost: 0.0}
]},
{label: "flow4", times: [
	{"label":"task1 (flow4)", task: "task1", "id":"2286f6f5-3b4a-49ed-b7dd-88c2f95826cb", "starting_time": 40, "ending_time": 42, delay: 0, cost: 0.0},
	{"label":"task2 (flow4)", task: "task2", "id":"1348dba8-2c44-44b2-b525-3d6fca9901e7", "starting_time": 42, "ending_time": 45, delay: 0, cost: 0.0},
	{"label":"task3 (flow4)", task: "task3", "id":"d86f136b-6b7b-418e-a9de-02c2089ebe6e", "starting_time": 45, "ending_time": 49, delay: 0, cost: 0.0}
]},
{label: "flow5", times: [
	{"label":"task1 (flow5)", task: "task1", "id":"1086bc8e-947f-497e-b476-33e880237c08", "starting_time": 50, "ending_time": 52, delay: 0, cost: 0.0},
	{"label":"task2 (flow5)", task: "task2", "id":"0dffb8d1-ec75-46f5-b6c1-2296f078546e", "starting_time": 52, "ending_time": 55, delay: 0, cost: 0.0},
	{"label":"task3 (flow5)", task: "task3", "id":"4fd12fc7-9878-4ac3-b1f5-123ae61567bc", "starting_time": 55, "ending_time": 59, delay: 0, cost: 0.0}
]},
{label: "flow6", times: [
	{"label":"task1 (flow6)", task: "task1", "id":"9d31bf5f-7b3f-4277-a632-95e837d02df1", "starting_time": 60, "ending_time": 62, delay: 0, cost: 0.0},
	{"label":"task2 (flow6)", task: "task2", "id":"50366cf2-8105-41cd-9c8b-d184ea876a15", "starting_time": 62, "ending_time": 65, delay: 0, cost: 0.0},
	{"label":"task3 (flow6)", task: "task3", "id":"740b879b-f032-4b17-b050-67fcead37e24", "starting_time": 65, "ending_time": 69, delay: 0, cost: 0.0}
]},
{label: "flow7", times: [
	{"label":"task1 (flow7)", task: "task1", "id":"b3695c13-f422-4c1e-b103-a08a6ad1e8dd", "starting_time": 70, "ending_time": 72, delay: 0, cost: 0.0},
	{"label":"task2 (flow7)", task: "task2", "id":"1aa1cf15-1e6d-4ecc-acf6-b016c353fcc7", "starting_time": 72, "ending_time": 75, delay: 0, cost: 0.0},
	{"label":"task3 (flow7)", task: "task3", "id":"bad8261e-24e1-435e-8de5-58e2899c5f76", "starting_time": 75, "ending_time": 79, delay: 0, cost: 0.0}
]},
{label: "flow8", times: [
	{"label":"task1 (flow8)", task: "task1", "id":"23e6905b-f79c-4999-94eb-72382bd9e47e", "starting_time": 80, "ending_time": 82, delay: 0, cost: 0.0},
	{"label":"task2 (flow8)", task: "task2", "id":"514ad6dd-a2ea-4015-a130-05e81bb49bc1", "starting_time": 82, "ending_time": 85, delay: 0, cost: 0.0},
	{"label":"task3 (flow8)", task: "task3", "id":"b26f33fa-8ab1-4f39-abaf-bf4258dd86c4", "starting_time": 85, "ending_time": 89, delay: 0, cost: 0.0}
]},
{label: "flow9", times: [
	{"label":"task1 (flow9)", task: "task1", "id":"c9797aa8-d4e0-4485-943e-39cf84a3281b", "starting_time": 90, "ending_time": 92, delay: 0, cost: 0.0},
	{"label":"task2 (flow9)", task: "task2", "id":"7b9c9283-45d3-473f-80bb-81d12d19bc0f", "starting_time": 92, "ending_time": 95, delay: 0, cost: 0.0},
	{"label":"task3 (flow9)", task: "task3", "id":"0976d314-5274-49f5-977a-bba9248038e5", "starting_time": 95, "ending_time": 99, delay: 0, cost: 0.0}
]},
];
