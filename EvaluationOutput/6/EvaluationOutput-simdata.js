var tasks = [
	"Script Task",
	"Service Task",
	"User Task A",
	"User Task B",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"034b6c2d-6155-4f94-a3f5-b57e3063a2a3", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"29d630d3-7c50-4c5f-b5c6-a072d8ece9d0", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"530a41a3-3198-4a75-a478-863d45fe0ee0", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"1b066bec-b7a0-4f42-8025-4a8ca297f071", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"28eede6f-74fb-4bbd-ad5a-dbc224252a0a", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"e7053276-b0c7-4314-b80f-e23966398901", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"c0cb057d-8256-4ab2-afdd-fe3093096ea5", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"7ccaf22c-ae43-47ce-b347-e266f5dadc93", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"f07b389c-94a4-4c92-8a68-0d992e558860", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"9c0eca56-dbf7-468c-9248-a2f81c6d8a92", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"40f59492-7066-442e-96dc-e79f3b7f7f95", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"546ccb0a-2b11-4d88-a1e9-67dfdb9b6db9", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"94e4d26a-3c51-4822-bc84-ea984c9d2d99", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"cf63a1ae-bf90-499f-b8e9-60ecf5fbf01c", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"8665d900-3628-4b2c-ae2b-7de8b41cc324", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"53f5eb00-9455-4334-94f5-b7383f207972", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"2fc71f7c-2777-4d08-bd3a-f093dd1ab9a0", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"86e9f6a4-457f-47c6-8efd-276696fc715c", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"95c5cb99-37dd-4af8-a6f3-e692ec2dcc9d", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"30862c0e-aa86-421f-8ad9-ebc44a65935d", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"4f36d1c5-3f31-4725-a9ea-fe4c7e2f60e9", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"ea736346-b7f7-4b05-99f1-10fb5e58b2bc", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"caef7d0b-4e6c-4d5a-867e-1d4bc18d1508", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"a84de2bf-6bf4-43b1-8e08-41b406e5fcad", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"daa1f6ad-bf2c-4b49-a51d-590e15fb2335", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"e04be8e2-4801-4787-b826-21f3eff69c43", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"d6d6c79a-63c9-47fc-8a8a-34bc143d8325", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"c58b0d1f-420d-40f6-8b07-0fef1ccd9017", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"37242cfc-e1bf-49d2-a8b9-99e9b5ea2010", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"3c61c7fb-7453-4432-91ca-c878dbaea281", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"88dee015-eaf2-4b57-a7c2-173472a2f273", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"effe9ac0-477b-4484-a372-00d162efbc0a", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"8616fbc7-c368-4f39-9d07-1574eae06790", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"6d8e9033-8433-4614-9999-e63d43a3881b", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"e1700f8d-6aed-418f-be50-5b54e3922239", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"15217305-9175-4565-b59b-4b6c63f3ed4c", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"405d48b8-4fdc-4af2-8842-d99ba362ec75", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"02947cec-2885-4849-99a6-cae0cf5921eb", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"67f886c7-be09-4cc1-87bf-ffbab5fb2a74", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"26ebe04f-a55c-4954-8799-89c5502de703", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"2e8b6ec5-39b1-448b-89ed-9291a856bc87", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"5a4e574f-8bbb-4135-9bf9-4f2473a0fa81", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"3b2e8f08-1a08-49ed-acf9-185cfab64e6a", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"c53e593a-c6e8-467f-a1bb-d2a7ec1d3860", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"f3cd908b-68b0-444e-a6d6-39a851655601", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"73ae872a-2eec-45d6-862e-9313bcf4abe2", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"dfb6d451-80b7-4511-9e84-ae991d434743", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"c9a4c7c5-9c9e-4def-870a-1379bee6a8a9", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"8c439b7c-635b-4d5c-8ef5-044792164b26", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"fa1afbc3-aecc-4f34-bd88-90e546804b2e", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"4490250f-a847-45a5-9860-d877b5587214", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"f90b6d9b-04d2-4195-a666-799c881afddd", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"a9e8b88a-787d-4b5b-9dd2-313bf8807cd0", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"b04c434e-6d66-4cda-b76e-2b080900a14e", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"7119df76-c465-4c3c-b22a-76c79c1bb8ed", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"9cdec940-ad0c-4d09-add2-0df5e2d7e4e0", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"7f6a471b-abf7-49c2-8607-35d8c79b5931", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"7b5ad6e1-3e63-48b5-bb9d-ecd492c5a5c2", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"5e87c65a-a0f8-4a3c-a46a-b2e73498b914", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"13c336ec-180d-4683-a86a-f6b4b007f776", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"6385bcba-cc69-414a-89f5-6f52a629918e", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"6f8ae582-28c6-4601-9af6-091745b6884e", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"99091d28-8425-4c89-9b1d-363cc60c442d", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"211ec3b3-a358-40a3-be27-ba5cc6c9f33e", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"21d4d519-ca2e-4986-8c37-02aaf1c2f331", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"98b077f6-a557-4def-9b78-4be2dde0a76d", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"5d3ac15d-ba2f-408c-9f67-bb7e614efe49", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"30437e26-a349-47c9-bbdb-76c72e2295a4", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"de6a4c1d-e7bb-46df-ad60-7abddfdc9bce", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"28e4972e-fc8f-49f8-b281-66ef0589b138", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"e78b5a5c-e0e2-4e00-a640-008409fdb400", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"ec069a56-95f8-45e8-8284-695adcecd3b5", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"130e4498-e614-40a0-a997-bb9af59e5acd", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"67bcd159-6adc-415a-ad25-705dda64d6da", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"74f2713b-a009-4dc4-bb00-84b5abdf109b", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"4f724410-13a0-48d1-9402-a50daac4f95e", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"0e313896-f836-42a8-96e8-1b246cd38d47", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"1b2ef29f-10cf-42b9-869d-198c4edd79ab", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"d589b534-c6bc-454a-9a1c-23d0d83bb55c", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"688ef79a-0539-488b-8a27-a30c78755b37", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"a3633c3b-3918-4aac-8114-f72a10353af2", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"687f1f7b-a66e-4848-af71-f03d3d3943ac", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"bd98dbfd-fc0b-49ce-ba2c-6111f070c699", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"61b4a8c8-ecc9-465a-9041-a6dea50f1e8b", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"73a6ecd1-d81b-4eea-86ec-9e778c50f2c2", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"a4b588da-fc21-4989-be1b-f31ae75778ab", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"daf28808-6160-48f5-a0e0-5ce4bf4640f9", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"568b7b81-524e-46f9-af92-41a40c1c0673", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"2e4e982e-4335-4738-a81d-fb72b023e871", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"80991d04-752e-4ec1-b7ef-ee17722bd03e", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"2b4db5d7-4158-4bf8-98da-6c9ebd1a4b9c", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"86c1aa48-0551-4a8d-ba2d-cfa84fd93ddf", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"68514fd3-b557-4dd1-a26d-e6622f76559a", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"e3917506-307d-404c-88b7-e7189cc3b684", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"b45da137-c7ef-4e5b-b7e6-ff81640e0d1c", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"a9452560-cab4-4c48-9a49-9d65186e09d7", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"33502d4d-5d63-476f-887f-cd70abc7ee4e", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"78db6829-e4fc-4c69-b11f-ac4a300c8322", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"7030b778-cce8-4f5a-8097-8bee30196a72", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"7f372394-7dd0-477d-95bc-cc5c159e67e2", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0}
]},
{label: "r2", times: [
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"c1c32206-1ee0-4c90-9b8c-933f46e2df73", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"d8f00476-6bea-429b-a4a2-9a24ee8ed3f0", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"dad1b83c-a9fa-4cb1-a092-b67ec7d84d80", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"fd0b7cd5-08d9-42d5-8519-065466c41403", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"196a974f-ad79-488b-98ec-674823ed3809", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"a2a82fab-8969-4319-a861-88f7e1efc6d1", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"431c82dd-388b-4a77-8a44-bd5ff84179ad", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"7f4d162e-43db-49ce-85fc-9df5acda52d0", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"916871b8-f207-4492-86d8-b12a18f0db45", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"cd94ebe9-eb0a-4072-8353-49772c4d263c", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"fb0d8cc6-259e-4ce5-a1d2-6ac28f5c5a5d", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"114d1e36-9534-4b80-8c80-48f31bdd2d13", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"4eeb7c3b-cfb1-43d3-b211-863b432eb954", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"86092eda-9c8f-4fa1-b950-8396a0640311", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"06e2c735-aa8a-4347-b97b-809e616c6f72", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"61256bf0-b15c-4ebf-b2c9-7be247dbfa58", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"f4c1a24f-4695-4290-a5ea-47dc9baf17b4", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"8fec3e90-1b2f-4271-a29f-88dbe5c8e3df", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"bca12fd3-e9a8-4dfc-ab41-6ebc963a8425", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"b673cc0d-b003-4352-8325-4d76db27f190", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"bd999728-793c-4c43-8f87-9dc265081b38", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"7dcbde39-953a-4d24-a976-31730ce278ad", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"872ef24b-b6d9-4489-9bda-0470585816c6", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"6fe54770-a7fc-4cdc-a797-e2cad84355b8", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"7cdf48ec-6b61-4a1b-bda6-5283220f4c48", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"c6451ca8-03e2-4bc7-a01f-e10d0791db6a", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"a3841aac-3e2b-41cc-9df5-bd3c5d957fe6", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"aee3872c-a7c5-4075-9cd7-3f9999f2092a", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"c60b813b-6060-45d1-95d5-863e39ad31c2", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"4a7eb44d-be9e-40f8-9e18-78fac11015b0", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"f3b4b3b9-32c2-4ac2-92c6-12eecbf48963", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"c188e0a4-5555-4bd3-9fa3-1370d8fba92b", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"3c7e2685-eb13-4146-95ee-bbd655093ff9", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"bce414f7-5b7e-4013-bdfc-0718db65987d", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"674174e3-46d9-4818-b2f2-19820ae5d63a", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"fc02a332-d5a9-4e56-bd13-78ac50e6f77f", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"285b1bdc-4d39-4e04-973e-72c57c01238e", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"081bc49d-7fde-44ee-b7cb-7b9c35a6dd6d", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"dc170923-f36a-44ab-aedf-22e9cd36244f", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"7f32ae8a-b70e-4188-9ed0-d2c982edbe8c", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"a54ac9b6-4783-4e23-b39d-5b8b298740a9", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"0c46143f-52d6-40e1-8483-e74302e47d60", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"ac69ad7b-ebe1-4f88-9ec7-83f05231404d", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"3e114b4d-e1fd-44dc-9c32-70cf9abb8e19", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"535c18f4-e30e-4a03-bb46-5a427620dcc6", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"87ecafae-43db-4be4-ac50-721a53eea9d6", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"cba8f9ff-820d-4f1e-8768-169aecfc7dd6", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0}
]},
{label: "r3", times: [
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"ae56e6e9-9c21-4c32-9cd6-ca34738eeba0", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"9d2fb795-e15c-44f0-9824-c056ebd1d377", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"637346d4-5da1-401c-89b4-aa625b161e82", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"e4b8e5ed-67e9-4dba-8b10-c9b7dc6737a0", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"11fc2676-85e8-4981-b749-1ecc5d75b359", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"285fcfcd-5a41-47c6-885a-ab04ee9c0527", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"6c33dc11-8a9d-4754-986a-92542df27892", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"30425039-19c1-418e-bb90-cb4c00f2f542", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"f3dc7f29-ec6e-4562-9cf5-5d646ce1c9b8", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"7ff7ee75-93e4-4b0f-8ae6-a9850963f96a", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"68bf839b-9200-41ea-9b19-9b9e332844bc", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"3b04bcd8-382a-4cda-9b43-84d3ebd5f77b", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"06aef17d-1964-4cbb-a909-9ba0ec38ce61", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"bffc6d5a-676b-4742-814b-bbc44853fb64", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"a6a84457-3a09-43ca-94c2-85b5f4b0efa8", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"21c0c31e-8f04-47f4-90b8-834966112176", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"5a051b31-db77-45a2-9331-91be1dc21f71", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"1a7f66a1-d9c9-4605-9274-6cf17bc68827", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"fd2c48cf-3a19-42c3-91b2-05264124be16", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"037c2bdc-4802-4c38-9205-a2748436df58", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"6ddec7ef-c559-4c5b-bec0-77c2d884131d", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"0201b07e-08df-4a17-832b-69112b07ed83", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"07547a16-fdb5-4fac-a9d6-29b4b11862f2", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"231cfefb-23fe-42c5-bd42-868ceb669ac2", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"a83e5e61-dcef-4fff-8b47-d367c20087d9", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"f2c8e064-b057-478d-9b57-5b863a6cebbf", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"022be491-b170-4dca-ac43-9ee186bf98c5", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"78390843-c8d5-45e2-bbff-8c5700caf7b0", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"08c9e80e-eb84-42c5-af89-ba122fe1fca3", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"9cf6c645-dd27-46d2-8b09-b0b8e5b2abb9", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"fbfa06b2-3a61-416b-97db-6f32122df995", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"6af84f1c-9d70-4350-b3ef-00486409ff56", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"7d4243bd-7e83-4b08-a255-d8930c7c3ff6", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"bb3b8197-f288-431d-b773-27f49d382837", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"55cb119f-8be8-4825-ac03-e56dd6652874", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"2596bf29-956f-4409-b367-702dd3165598", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"de9d0f79-86fa-4c7d-b876-fde9954c004c", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"0bd509e4-e9b8-41ff-8a59-f5a91dc91eb8", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"267e57db-7305-4349-bf0a-7e61fcbd1e80", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"698548c7-1f4e-4527-8cad-4f44e3d633af", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"f599b872-9a9d-4545-9e21-20ef7321ae2d", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"99e7bca2-0981-4dff-a8e1-5127f7580aa3", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"14e0d9b2-fc76-48a9-be22-b2c3fa5ca387", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"119e3d6d-81f3-4318-bebe-395337436ce9", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"82467708-b61f-4175-ad9b-18b9f991e769", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"7757ae68-cfd7-4b3e-9cb0-7bf873a4cdcb", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"80092119-0d94-4131-b403-8cf2325537db", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"1bebc934-1252-4f80-8357-29e2e18967a1", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"fb631722-c936-4948-9e6f-a6cc72bd78e1", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"df650867-d1fe-469f-a060-865b4fde8541", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"325d02b5-9080-4fb7-a010-6494f983b08f", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"64a5ac1f-8373-4625-a85c-f48e4f00b99c", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"2a1b5030-e618-4984-816c-c919aeff559b", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0}
]},
{label: "r4", times: [
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"37fb02c6-686c-494c-8179-cf31ce2949d8", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"38a5af53-4cc9-4edd-8edd-a5ba0178d541", "starting_time": 30, "ending_time": 35, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"20680d74-e795-4f08-8081-14b2c59acc90", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"a33daf80-362b-4dd2-af45-b7d7a1a26ae0", "starting_time": 70, "ending_time": 75, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"aa4dca29-6297-4960-a6a4-333e992f7c47", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"24972d9d-922d-4a5e-89e1-00b99583e654", "starting_time": 110, "ending_time": 115, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"74b67379-9007-444a-9d48-1c35c2637a9b", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"94926e3b-adea-42bd-ab5d-8e43b95e57ab", "starting_time": 150, "ending_time": 155, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"0dc1e9a9-1026-4ed6-bc15-06146bad0d60", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"f8e63209-c492-4a2b-9895-b2bd365d7b18", "starting_time": 190, "ending_time": 195, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"634c5d2f-b21b-4b1f-a9eb-87841668a656", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"e1f4f015-416b-4438-bf24-538b84abc7c5", "starting_time": 230, "ending_time": 235, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"83d6fcf0-f7d4-4532-8252-2bb800582b23", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"9e45b114-1633-4196-8bd1-2c6dd2602cad", "starting_time": 270, "ending_time": 275, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"9f4b6dc4-046a-44f0-83a1-1e3aa6bfe145", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"befaab62-5575-46c1-9776-2e75e97c8bc5", "starting_time": 310, "ending_time": 315, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"3486e6a8-dca8-4b69-a6ba-20661c8d7a1c", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"1fee168c-7832-4032-8762-5160541b72b9", "starting_time": 350, "ending_time": 355, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"b601a718-f742-4dc8-8f9a-17d7c813c86e", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"43d6ab28-b235-4c20-a6d8-86d7433211d5", "starting_time": 390, "ending_time": 395, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"e7bd2930-d8f4-4810-b1f5-935d17d7c1ef", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"d2a62712-26fd-46cc-9e29-2ee0296e468d", "starting_time": 430, "ending_time": 435, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"af3eb798-6d87-44e3-ae89-526e64a07216", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"677386db-31f2-4acd-b379-e0b029fa4eba", "starting_time": 470, "ending_time": 475, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"39e5c01d-b99e-4bf0-b1a5-7b5c09571654", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"b1da7fe0-01e3-4264-b9fe-64166762938f", "starting_time": 510, "ending_time": 515, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"7db46c3d-24c3-4520-956c-7d0193ea16c3", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"2efd38b1-8e41-4572-9b22-c9a32f5b3751", "starting_time": 550, "ending_time": 555, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"1633aef9-791d-4712-b385-9118254fa493", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"3b3d9427-9906-4208-a365-ef1869a4cc6e", "starting_time": 590, "ending_time": 595, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"9238ab50-38fd-4880-859c-cc3289360a97", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"518c66e6-978f-4a9a-9251-f779200dd2c5", "starting_time": 630, "ending_time": 635, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"9037a6ff-3c8d-465c-9872-c4c3518f9d93", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"2a6f4f3d-7331-4186-80b8-c30b70bcd385", "starting_time": 670, "ending_time": 675, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"107eab1b-a544-4673-90c7-ead30d297b3f", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"87938399-43f5-4901-a815-f3aec629e28a", "starting_time": 710, "ending_time": 715, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"fcbd416b-470b-4af5-88d1-ebf7c53d8e02", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"ef56529b-fea2-4a6b-a7aa-f98e47852be4", "starting_time": 750, "ending_time": 755, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"ed14656c-f7a5-47bb-8536-9a275e6c9c73", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"2709c647-69e3-43a5-a13d-c06d4689667c", "starting_time": 790, "ending_time": 795, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"2ff1bc4b-e733-429b-8e61-0bf9f006d2df", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"27117bdd-70c6-4c9d-8130-e17a281378f3", "starting_time": 830, "ending_time": 835, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"700e8a42-319d-48ad-8f2b-1d297373ff5f", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"f3adab64-11d3-494d-ae37-79ef23c36bb9", "starting_time": 870, "ending_time": 875, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"de594b81-ff29-4eb3-95fd-c13759635da8", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"3828d4af-f84b-41ca-a00e-d3e844d2359a", "starting_time": 910, "ending_time": 915, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"d678a8a4-59c9-4995-b735-0f6239ce39ab", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"a63f09c5-2a38-4b63-af3b-3d54d4fccf38", "starting_time": 950, "ending_time": 955, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"dca7ed8c-473b-4b8e-8d67-0d871363b3d5", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"19a21e34-107c-4e1f-a9f8-4fb5a01d9bf0", "starting_time": 990, "ending_time": 995, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"ed94e039-5d4d-4196-89d2-83d109a41c03", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"0263b468-d654-4265-ae11-15a583cdfaf4", "starting_time": 1030, "ending_time": 1035, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"e61dfa51-82c6-4f58-a373-eac1644b0130", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"65ffd40d-b5ac-46f6-b03e-2bf7d2d464a3", "starting_time": 1070, "ending_time": 1075, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"fd40c3f1-0d13-4aca-a160-fc5ea4211b25", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"7856fe52-d865-4c37-9d19-033d8a0d30bb", "starting_time": 1110, "ending_time": 1115, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"f20aafdd-e3c4-4a52-84fe-f06ad6c28a65", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"65b221eb-a609-48b5-9e5a-18b4dbe3b237", "starting_time": 1150, "ending_time": 1155, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"330f63db-9b37-46d2-8e42-1266449ac94b", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"dab71ae3-e587-4507-977e-923af58bc721", "starting_time": 1190, "ending_time": 1195, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"3954b0d7-ed72-4102-acf2-80c99b35419a", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"ac37d9b2-f7e7-48a3-b823-c9b69f46db62", "starting_time": 1230, "ending_time": 1235, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"3f051fdf-d08f-4399-8ecf-70556d9d3f50", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"02ca83a4-1d09-4f09-9f93-2266d8e5e0f8", "starting_time": 1270, "ending_time": 1275, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"341539c5-f960-40ba-a50e-befeda81f43c", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"5f03cea0-d192-41d0-b21f-f505c4ec74cd", "starting_time": 1310, "ending_time": 1315, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"f56f5053-d5a8-4ae4-8ff9-a1ace19bc2dd", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"46f7edb5-7028-43a0-9549-337d2be12550", "starting_time": 1350, "ending_time": 1355, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"7b82bb43-90a3-407b-9152-7756567d734b", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"15e7c02b-0db9-43e1-8645-ce8815f454c1", "starting_time": 1390, "ending_time": 1395, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"6bb45a24-bf5a-4449-ba7c-624b2313a686", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"795017c0-aa1b-4226-a582-bdca401b0397", "starting_time": 1430, "ending_time": 1435, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"6fdbea5b-3b16-42d2-a319-c122f049dd9a", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"5641445d-44e9-4589-8544-f48cc17d2ab6", "starting_time": 1470, "ending_time": 1475, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"5ef6f2e8-ed46-4cb1-aeac-1c60ba2c6baf", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"5b118054-190a-4e74-9a28-a0c7e9be9091", "starting_time": 1510, "ending_time": 1515, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"a7d700f5-b176-4611-b9f6-222811d73490", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"d47d547e-ca9d-43aa-8538-50ac2f2bd8aa", "starting_time": 1550, "ending_time": 1555, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"563cfac6-6230-4679-9d19-1a51b281f708", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"3105a872-8765-4aa2-81c3-94c3a34456d9", "starting_time": 1590, "ending_time": 1595, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"452e5ea4-3060-4dfe-9330-033bef29a46c", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"3e567f19-8a01-4897-9496-4b29edc8d8a5", "starting_time": 1630, "ending_time": 1635, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"2489a723-bfce-4898-8d73-27fa9e00a11c", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"ca3d59bb-339d-4253-a980-a201cb0676da", "starting_time": 1670, "ending_time": 1675, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"c3de68f6-35e7-49a6-ab53-c7e78d3496a6", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"0fc202ce-1387-4604-a060-2565e2d92c83", "starting_time": 1710, "ending_time": 1715, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"17aae7e1-453e-4f47-ae10-5623af0b4f7e", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"a26b4b42-a666-425e-89d0-e3f3ec5964be", "starting_time": 1750, "ending_time": 1755, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"742e6925-16b2-4913-8885-a03d35f64efc", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"5a441e21-f64d-41f6-9bc3-b9fd1ef18af0", "starting_time": 1790, "ending_time": 1795, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"998a2945-9743-43c1-b0c3-6c8e70525919", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"3cdf5dc8-e3f2-46e6-93ab-16213bd4ac9e", "starting_time": 1830, "ending_time": 1835, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"9b98507a-ecd7-426a-92de-fdc9224c8cba", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"0b5c667f-8ed2-44bd-bc18-0b883e645a64", "starting_time": 1870, "ending_time": 1875, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"607f0497-af9d-443b-b080-feb471e3d79f", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"6e705c8b-1c1f-4478-b0cc-afa8ef803a1d", "starting_time": 1910, "ending_time": 1915, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"a844ddaf-d7af-4b5c-bee9-8c9bdebf4930", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"3bd9a7a1-7831-4ca8-876c-d9b55b391448", "starting_time": 1950, "ending_time": 1955, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"ff5a8095-9e85-45a8-85ac-7eaa1d87008c", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"b17f6873-3013-4b10-bb7e-41a4d4a2cbc7", "starting_time": 1990, "ending_time": 1995, delay: 0, cost: 0}
]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"034b6c2d-6155-4f94-a3f5-b57e3063a2a3", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"c1c32206-1ee0-4c90-9b8c-933f46e2df73", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"37fb02c6-686c-494c-8179-cf31ce2949d8", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"29d630d3-7c50-4c5f-b5c6-a072d8ece9d0", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"ae56e6e9-9c21-4c32-9cd6-ca34738eeba0", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"38a5af53-4cc9-4edd-8edd-a5ba0178d541", "starting_time": 30, "ending_time": 35, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"40f59492-7066-442e-96dc-e79f3b7f7f95", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"dad1b83c-a9fa-4cb1-a092-b67ec7d84d80", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"634c5d2f-b21b-4b1f-a9eb-87841668a656", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"546ccb0a-2b11-4d88-a1e9-67dfdb9b6db9", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"f3dc7f29-ec6e-4562-9cf5-5d646ce1c9b8", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"e1f4f015-416b-4438-bf24-538b84abc7c5", "starting_time": 230, "ending_time": 235, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"94e4d26a-3c51-4822-bc84-ea984c9d2d99", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"7ff7ee75-93e4-4b0f-8ae6-a9850963f96a", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"83d6fcf0-f7d4-4532-8252-2bb800582b23", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"cf63a1ae-bf90-499f-b8e9-60ecf5fbf01c", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"fd0b7cd5-08d9-42d5-8519-065466c41403", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"9e45b114-1633-4196-8bd1-2c6dd2602cad", "starting_time": 270, "ending_time": 275, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"8665d900-3628-4b2c-ae2b-7de8b41cc324", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"68bf839b-9200-41ea-9b19-9b9e332844bc", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"9f4b6dc4-046a-44f0-83a1-1e3aa6bfe145", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"53f5eb00-9455-4334-94f5-b7383f207972", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"196a974f-ad79-488b-98ec-674823ed3809", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"befaab62-5575-46c1-9776-2e75e97c8bc5", "starting_time": 310, "ending_time": 315, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"2fc71f7c-2777-4d08-bd3a-f093dd1ab9a0", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"3b04bcd8-382a-4cda-9b43-84d3ebd5f77b", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"3486e6a8-dca8-4b69-a6ba-20661c8d7a1c", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"86e9f6a4-457f-47c6-8efd-276696fc715c", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"06aef17d-1964-4cbb-a909-9ba0ec38ce61", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"1fee168c-7832-4032-8762-5160541b72b9", "starting_time": 350, "ending_time": 355, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"95c5cb99-37dd-4af8-a6f3-e692ec2dcc9d", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"a2a82fab-8969-4319-a861-88f7e1efc6d1", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"b601a718-f742-4dc8-8f9a-17d7c813c86e", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"30862c0e-aa86-421f-8ad9-ebc44a65935d", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"bffc6d5a-676b-4742-814b-bbc44853fb64", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"43d6ab28-b235-4c20-a6d8-86d7433211d5", "starting_time": 390, "ending_time": 395, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"530a41a3-3198-4a75-a478-863d45fe0ee0", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"9d2fb795-e15c-44f0-9824-c056ebd1d377", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"20680d74-e795-4f08-8081-14b2c59acc90", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"4f36d1c5-3f31-4725-a9ea-fe4c7e2f60e9", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"431c82dd-388b-4a77-8a44-bd5ff84179ad", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"e7bd2930-d8f4-4810-b1f5-935d17d7c1ef", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"ea736346-b7f7-4b05-99f1-10fb5e58b2bc", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"7f4d162e-43db-49ce-85fc-9df5acda52d0", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"d2a62712-26fd-46cc-9e29-2ee0296e468d", "starting_time": 430, "ending_time": 435, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"caef7d0b-4e6c-4d5a-867e-1d4bc18d1508", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"916871b8-f207-4492-86d8-b12a18f0db45", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"af3eb798-6d87-44e3-ae89-526e64a07216", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"a84de2bf-6bf4-43b1-8e08-41b406e5fcad", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"a6a84457-3a09-43ca-94c2-85b5f4b0efa8", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"677386db-31f2-4acd-b379-e0b029fa4eba", "starting_time": 470, "ending_time": 475, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"daa1f6ad-bf2c-4b49-a51d-590e15fb2335", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"cd94ebe9-eb0a-4072-8353-49772c4d263c", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"39e5c01d-b99e-4bf0-b1a5-7b5c09571654", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"e04be8e2-4801-4787-b826-21f3eff69c43", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"fb0d8cc6-259e-4ce5-a1d2-6ac28f5c5a5d", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"b1da7fe0-01e3-4264-b9fe-64166762938f", "starting_time": 510, "ending_time": 515, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"d6d6c79a-63c9-47fc-8a8a-34bc143d8325", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"21c0c31e-8f04-47f4-90b8-834966112176", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"7db46c3d-24c3-4520-956c-7d0193ea16c3", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"c58b0d1f-420d-40f6-8b07-0fef1ccd9017", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"5a051b31-db77-45a2-9331-91be1dc21f71", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"2efd38b1-8e41-4572-9b22-c9a32f5b3751", "starting_time": 550, "ending_time": 555, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"37242cfc-e1bf-49d2-a8b9-99e9b5ea2010", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"114d1e36-9534-4b80-8c80-48f31bdd2d13", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"1633aef9-791d-4712-b385-9118254fa493", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"3c61c7fb-7453-4432-91ca-c878dbaea281", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"4eeb7c3b-cfb1-43d3-b211-863b432eb954", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"3b3d9427-9906-4208-a365-ef1869a4cc6e", "starting_time": 590, "ending_time": 595, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"1b066bec-b7a0-4f42-8025-4a8ca297f071", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"637346d4-5da1-401c-89b4-aa625b161e82", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"a33daf80-362b-4dd2-af45-b7d7a1a26ae0", "starting_time": 70, "ending_time": 75, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"88dee015-eaf2-4b57-a7c2-173472a2f273", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"86092eda-9c8f-4fa1-b950-8396a0640311", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"9238ab50-38fd-4880-859c-cc3289360a97", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"effe9ac0-477b-4484-a372-00d162efbc0a", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"1a7f66a1-d9c9-4605-9274-6cf17bc68827", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"518c66e6-978f-4a9a-9251-f779200dd2c5", "starting_time": 630, "ending_time": 635, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"8616fbc7-c368-4f39-9d07-1574eae06790", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"fd2c48cf-3a19-42c3-91b2-05264124be16", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"9037a6ff-3c8d-465c-9872-c4c3518f9d93", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"6d8e9033-8433-4614-9999-e63d43a3881b", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"037c2bdc-4802-4c38-9205-a2748436df58", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"2a6f4f3d-7331-4186-80b8-c30b70bcd385", "starting_time": 670, "ending_time": 675, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"e1700f8d-6aed-418f-be50-5b54e3922239", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"6ddec7ef-c559-4c5b-bec0-77c2d884131d", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"107eab1b-a544-4673-90c7-ead30d297b3f", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"15217305-9175-4565-b59b-4b6c63f3ed4c", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"0201b07e-08df-4a17-832b-69112b07ed83", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"87938399-43f5-4901-a815-f3aec629e28a", "starting_time": 710, "ending_time": 715, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"405d48b8-4fdc-4af2-8842-d99ba362ec75", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"07547a16-fdb5-4fac-a9d6-29b4b11862f2", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"fcbd416b-470b-4af5-88d1-ebf7c53d8e02", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"02947cec-2885-4849-99a6-cae0cf5921eb", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"06e2c735-aa8a-4347-b97b-809e616c6f72", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"ef56529b-fea2-4a6b-a7aa-f98e47852be4", "starting_time": 750, "ending_time": 755, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"67f886c7-be09-4cc1-87bf-ffbab5fb2a74", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"61256bf0-b15c-4ebf-b2c9-7be247dbfa58", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"ed14656c-f7a5-47bb-8536-9a275e6c9c73", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"26ebe04f-a55c-4954-8799-89c5502de703", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"f4c1a24f-4695-4290-a5ea-47dc9baf17b4", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"2709c647-69e3-43a5-a13d-c06d4689667c", "starting_time": 790, "ending_time": 795, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"28eede6f-74fb-4bbd-ad5a-dbc224252a0a", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"e4b8e5ed-67e9-4dba-8b10-c9b7dc6737a0", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"aa4dca29-6297-4960-a6a4-333e992f7c47", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"2e8b6ec5-39b1-448b-89ed-9291a856bc87", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"8fec3e90-1b2f-4271-a29f-88dbe5c8e3df", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"2ff1bc4b-e733-429b-8e61-0bf9f006d2df", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"5a4e574f-8bbb-4135-9bf9-4f2473a0fa81", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"bca12fd3-e9a8-4dfc-ab41-6ebc963a8425", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"27117bdd-70c6-4c9d-8130-e17a281378f3", "starting_time": 830, "ending_time": 835, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"3b2e8f08-1a08-49ed-acf9-185cfab64e6a", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"b673cc0d-b003-4352-8325-4d76db27f190", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"700e8a42-319d-48ad-8f2b-1d297373ff5f", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"c53e593a-c6e8-467f-a1bb-d2a7ec1d3860", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"231cfefb-23fe-42c5-bd42-868ceb669ac2", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"f3adab64-11d3-494d-ae37-79ef23c36bb9", "starting_time": 870, "ending_time": 875, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"f3cd908b-68b0-444e-a6d6-39a851655601", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"bd999728-793c-4c43-8f87-9dc265081b38", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"de594b81-ff29-4eb3-95fd-c13759635da8", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"73ae872a-2eec-45d6-862e-9313bcf4abe2", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"a83e5e61-dcef-4fff-8b47-d367c20087d9", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"3828d4af-f84b-41ca-a00e-d3e844d2359a", "starting_time": 910, "ending_time": 915, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"dfb6d451-80b7-4511-9e84-ae991d434743", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"f2c8e064-b057-478d-9b57-5b863a6cebbf", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"d678a8a4-59c9-4995-b735-0f6239ce39ab", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"c9a4c7c5-9c9e-4def-870a-1379bee6a8a9", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"7dcbde39-953a-4d24-a976-31730ce278ad", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"a63f09c5-2a38-4b63-af3b-3d54d4fccf38", "starting_time": 950, "ending_time": 955, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"8c439b7c-635b-4d5c-8ef5-044792164b26", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"872ef24b-b6d9-4489-9bda-0470585816c6", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"dca7ed8c-473b-4b8e-8d67-0d871363b3d5", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"fa1afbc3-aecc-4f34-bd88-90e546804b2e", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"022be491-b170-4dca-ac43-9ee186bf98c5", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"19a21e34-107c-4e1f-a9f8-4fb5a01d9bf0", "starting_time": 990, "ending_time": 995, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"e7053276-b0c7-4314-b80f-e23966398901", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"11fc2676-85e8-4981-b749-1ecc5d75b359", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"24972d9d-922d-4a5e-89e1-00b99583e654", "starting_time": 110, "ending_time": 115, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"4490250f-a847-45a5-9860-d877b5587214", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"6fe54770-a7fc-4cdc-a797-e2cad84355b8", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"ed94e039-5d4d-4196-89d2-83d109a41c03", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"f90b6d9b-04d2-4195-a666-799c881afddd", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"78390843-c8d5-45e2-bbff-8c5700caf7b0", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"0263b468-d654-4265-ae11-15a583cdfaf4", "starting_time": 1030, "ending_time": 1035, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"a9e8b88a-787d-4b5b-9dd2-313bf8807cd0", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"7cdf48ec-6b61-4a1b-bda6-5283220f4c48", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"e61dfa51-82c6-4f58-a373-eac1644b0130", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"b04c434e-6d66-4cda-b76e-2b080900a14e", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"08c9e80e-eb84-42c5-af89-ba122fe1fca3", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"65ffd40d-b5ac-46f6-b03e-2bf7d2d464a3", "starting_time": 1070, "ending_time": 1075, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"7119df76-c465-4c3c-b22a-76c79c1bb8ed", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"9cf6c645-dd27-46d2-8b09-b0b8e5b2abb9", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"fd40c3f1-0d13-4aca-a160-fc5ea4211b25", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"9cdec940-ad0c-4d09-add2-0df5e2d7e4e0", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"c6451ca8-03e2-4bc7-a01f-e10d0791db6a", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"7856fe52-d865-4c37-9d19-033d8a0d30bb", "starting_time": 1110, "ending_time": 1115, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"7f6a471b-abf7-49c2-8607-35d8c79b5931", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"a3841aac-3e2b-41cc-9df5-bd3c5d957fe6", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"f20aafdd-e3c4-4a52-84fe-f06ad6c28a65", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"7b5ad6e1-3e63-48b5-bb9d-ecd492c5a5c2", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"fbfa06b2-3a61-416b-97db-6f32122df995", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"65b221eb-a609-48b5-9e5a-18b4dbe3b237", "starting_time": 1150, "ending_time": 1155, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"5e87c65a-a0f8-4a3c-a46a-b2e73498b914", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"6af84f1c-9d70-4350-b3ef-00486409ff56", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"330f63db-9b37-46d2-8e42-1266449ac94b", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"13c336ec-180d-4683-a86a-f6b4b007f776", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"aee3872c-a7c5-4075-9cd7-3f9999f2092a", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"dab71ae3-e587-4507-977e-923af58bc721", "starting_time": 1190, "ending_time": 1195, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"c0cb057d-8256-4ab2-afdd-fe3093096ea5", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"285fcfcd-5a41-47c6-885a-ab04ee9c0527", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"74b67379-9007-444a-9d48-1c35c2637a9b", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"6385bcba-cc69-414a-89f5-6f52a629918e", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"7d4243bd-7e83-4b08-a255-d8930c7c3ff6", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"3954b0d7-ed72-4102-acf2-80c99b35419a", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"6f8ae582-28c6-4601-9af6-091745b6884e", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"bb3b8197-f288-431d-b773-27f49d382837", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"ac37d9b2-f7e7-48a3-b823-c9b69f46db62", "starting_time": 1230, "ending_time": 1235, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"99091d28-8425-4c89-9b1d-363cc60c442d", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"55cb119f-8be8-4825-ac03-e56dd6652874", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"3f051fdf-d08f-4399-8ecf-70556d9d3f50", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"211ec3b3-a358-40a3-be27-ba5cc6c9f33e", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"c60b813b-6060-45d1-95d5-863e39ad31c2", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"02ca83a4-1d09-4f09-9f93-2266d8e5e0f8", "starting_time": 1270, "ending_time": 1275, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"21d4d519-ca2e-4986-8c37-02aaf1c2f331", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"2596bf29-956f-4409-b367-702dd3165598", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"341539c5-f960-40ba-a50e-befeda81f43c", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"98b077f6-a557-4def-9b78-4be2dde0a76d", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"de9d0f79-86fa-4c7d-b876-fde9954c004c", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"5f03cea0-d192-41d0-b21f-f505c4ec74cd", "starting_time": 1310, "ending_time": 1315, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"5d3ac15d-ba2f-408c-9f67-bb7e614efe49", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"4a7eb44d-be9e-40f8-9e18-78fac11015b0", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"f56f5053-d5a8-4ae4-8ff9-a1ace19bc2dd", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"30437e26-a349-47c9-bbdb-76c72e2295a4", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"f3b4b3b9-32c2-4ac2-92c6-12eecbf48963", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"46f7edb5-7028-43a0-9549-337d2be12550", "starting_time": 1350, "ending_time": 1355, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"de6a4c1d-e7bb-46df-ad60-7abddfdc9bce", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"0bd509e4-e9b8-41ff-8a59-f5a91dc91eb8", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"7b82bb43-90a3-407b-9152-7756567d734b", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"28e4972e-fc8f-49f8-b281-66ef0589b138", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"c188e0a4-5555-4bd3-9fa3-1370d8fba92b", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"15e7c02b-0db9-43e1-8645-ce8815f454c1", "starting_time": 1390, "ending_time": 1395, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"7ccaf22c-ae43-47ce-b347-e266f5dadc93", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"d8f00476-6bea-429b-a4a2-9a24ee8ed3f0", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"94926e3b-adea-42bd-ab5d-8e43b95e57ab", "starting_time": 150, "ending_time": 155, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"e78b5a5c-e0e2-4e00-a640-008409fdb400", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"267e57db-7305-4349-bf0a-7e61fcbd1e80", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"6bb45a24-bf5a-4449-ba7c-624b2313a686", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"ec069a56-95f8-45e8-8284-695adcecd3b5", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"698548c7-1f4e-4527-8cad-4f44e3d633af", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"795017c0-aa1b-4226-a582-bdca401b0397", "starting_time": 1430, "ending_time": 1435, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"130e4498-e614-40a0-a997-bb9af59e5acd", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"3c7e2685-eb13-4146-95ee-bbd655093ff9", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"6fdbea5b-3b16-42d2-a319-c122f049dd9a", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"67bcd159-6adc-415a-ad25-705dda64d6da", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"bce414f7-5b7e-4013-bdfc-0718db65987d", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"5641445d-44e9-4589-8544-f48cc17d2ab6", "starting_time": 1470, "ending_time": 1475, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"74f2713b-a009-4dc4-bb00-84b5abdf109b", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"f599b872-9a9d-4545-9e21-20ef7321ae2d", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"5ef6f2e8-ed46-4cb1-aeac-1c60ba2c6baf", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"4f724410-13a0-48d1-9402-a50daac4f95e", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"674174e3-46d9-4818-b2f2-19820ae5d63a", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"5b118054-190a-4e74-9a28-a0c7e9be9091", "starting_time": 1510, "ending_time": 1515, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"0e313896-f836-42a8-96e8-1b246cd38d47", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"fc02a332-d5a9-4e56-bd13-78ac50e6f77f", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"a7d700f5-b176-4611-b9f6-222811d73490", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"1b2ef29f-10cf-42b9-869d-198c4edd79ab", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"285b1bdc-4d39-4e04-973e-72c57c01238e", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"d47d547e-ca9d-43aa-8538-50ac2f2bd8aa", "starting_time": 1550, "ending_time": 1555, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"d589b534-c6bc-454a-9a1c-23d0d83bb55c", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"081bc49d-7fde-44ee-b7cb-7b9c35a6dd6d", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"563cfac6-6230-4679-9d19-1a51b281f708", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"688ef79a-0539-488b-8a27-a30c78755b37", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"dc170923-f36a-44ab-aedf-22e9cd36244f", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"3105a872-8765-4aa2-81c3-94c3a34456d9", "starting_time": 1590, "ending_time": 1595, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"f07b389c-94a4-4c92-8a68-0d992e558860", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"6c33dc11-8a9d-4754-986a-92542df27892", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"0dc1e9a9-1026-4ed6-bc15-06146bad0d60", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"a3633c3b-3918-4aac-8114-f72a10353af2", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"99e7bca2-0981-4dff-a8e1-5127f7580aa3", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"452e5ea4-3060-4dfe-9330-033bef29a46c", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"687f1f7b-a66e-4848-af71-f03d3d3943ac", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"14e0d9b2-fc76-48a9-be22-b2c3fa5ca387", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"3e567f19-8a01-4897-9496-4b29edc8d8a5", "starting_time": 1630, "ending_time": 1635, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"bd98dbfd-fc0b-49ce-ba2c-6111f070c699", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"7f32ae8a-b70e-4188-9ed0-d2c982edbe8c", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"2489a723-bfce-4898-8d73-27fa9e00a11c", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"61b4a8c8-ecc9-465a-9041-a6dea50f1e8b", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"a54ac9b6-4783-4e23-b39d-5b8b298740a9", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"ca3d59bb-339d-4253-a980-a201cb0676da", "starting_time": 1670, "ending_time": 1675, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"73a6ecd1-d81b-4eea-86ec-9e778c50f2c2", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"119e3d6d-81f3-4318-bebe-395337436ce9", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"c3de68f6-35e7-49a6-ab53-c7e78d3496a6", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"a4b588da-fc21-4989-be1b-f31ae75778ab", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"82467708-b61f-4175-ad9b-18b9f991e769", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"0fc202ce-1387-4604-a060-2565e2d92c83", "starting_time": 1710, "ending_time": 1715, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"daf28808-6160-48f5-a0e0-5ce4bf4640f9", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"7757ae68-cfd7-4b3e-9cb0-7bf873a4cdcb", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"17aae7e1-453e-4f47-ae10-5623af0b4f7e", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"568b7b81-524e-46f9-af92-41a40c1c0673", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"0c46143f-52d6-40e1-8483-e74302e47d60", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"a26b4b42-a666-425e-89d0-e3f3ec5964be", "starting_time": 1750, "ending_time": 1755, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"2e4e982e-4335-4738-a81d-fb72b023e871", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"ac69ad7b-ebe1-4f88-9ec7-83f05231404d", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"742e6925-16b2-4913-8885-a03d35f64efc", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"80991d04-752e-4ec1-b7ef-ee17722bd03e", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"80092119-0d94-4131-b403-8cf2325537db", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"5a441e21-f64d-41f6-9bc3-b9fd1ef18af0", "starting_time": 1790, "ending_time": 1795, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"9c0eca56-dbf7-468c-9248-a2f81c6d8a92", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"30425039-19c1-418e-bb90-cb4c00f2f542", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"f8e63209-c492-4a2b-9895-b2bd365d7b18", "starting_time": 190, "ending_time": 195, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"2b4db5d7-4158-4bf8-98da-6c9ebd1a4b9c", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"1bebc934-1252-4f80-8357-29e2e18967a1", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"998a2945-9743-43c1-b0c3-6c8e70525919", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"86c1aa48-0551-4a8d-ba2d-cfa84fd93ddf", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"3e114b4d-e1fd-44dc-9c32-70cf9abb8e19", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"3cdf5dc8-e3f2-46e6-93ab-16213bd4ac9e", "starting_time": 1830, "ending_time": 1835, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"68514fd3-b557-4dd1-a26d-e6622f76559a", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"fb631722-c936-4948-9e6f-a6cc72bd78e1", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"9b98507a-ecd7-426a-92de-fdc9224c8cba", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"e3917506-307d-404c-88b7-e7189cc3b684", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"df650867-d1fe-469f-a060-865b4fde8541", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"0b5c667f-8ed2-44bd-bc18-0b883e645a64", "starting_time": 1870, "ending_time": 1875, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"b45da137-c7ef-4e5b-b7e6-ff81640e0d1c", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"325d02b5-9080-4fb7-a010-6494f983b08f", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"607f0497-af9d-443b-b080-feb471e3d79f", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"a9452560-cab4-4c48-9a49-9d65186e09d7", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"535c18f4-e30e-4a03-bb46-5a427620dcc6", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"6e705c8b-1c1f-4478-b0cc-afa8ef803a1d", "starting_time": 1910, "ending_time": 1915, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"33502d4d-5d63-476f-887f-cd70abc7ee4e", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"87ecafae-43db-4be4-ac50-721a53eea9d6", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"a844ddaf-d7af-4b5c-bee9-8c9bdebf4930", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"78db6829-e4fc-4c69-b11f-ac4a300c8322", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"64a5ac1f-8373-4625-a85c-f48e4f00b99c", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"3bd9a7a1-7831-4ca8-876c-d9b55b391448", "starting_time": 1950, "ending_time": 1955, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"7030b778-cce8-4f5a-8097-8bee30196a72", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"cba8f9ff-820d-4f1e-8768-169aecfc7dd6", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"ff5a8095-9e85-45a8-85ac-7eaa1d87008c", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"7f372394-7dd0-477d-95bc-cc5c159e67e2", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"2a1b5030-e618-4984-816c-c919aeff559b", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"b17f6873-3013-4b10-bb7e-41a4d4a2cbc7", "starting_time": 1990, "ending_time": 1995, delay: 0, cost: 0}
]},
];
