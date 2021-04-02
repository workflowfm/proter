var tasks = [
	"Script Task",
	"Service Task",
	"User Task A",
	"User Task B",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"fde78f37-d735-47d1-aed6-14c059bb306e", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"0f96e0b6-080c-4150-945e-4092acccf536", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"6ceaf7ea-ad30-43ac-ab8f-7c35c5197bdf", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"eaca1646-fe9a-4b44-b8a4-190712685215", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"ff9c3870-35e2-472e-afa4-17903b3fdf4e", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"e0c7b15f-c765-4a8a-967d-847e95610aa7", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"d5b15670-6d87-439b-b770-d06ef109b562", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"1acc4b94-c8f3-4791-8b65-956ea97fc410", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"a8d7e6c3-e5d6-4700-89b7-529f3d2997d9", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"470c4fed-53d8-43ac-8e83-b1ea6df22e39", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"b9d10f80-1c7b-4322-bf38-75ffd57f6895", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"f2889528-977b-4823-ae94-172432f70537", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"bef34e9a-c4cb-4e93-9598-9108cff2a937", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"5cf78d91-2d56-4389-92a8-78ee8be9d3e1", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"63db68fe-329d-403c-8ad8-5c9c2dae7fb6", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"f6b3ee01-9676-4180-9605-b37ed33c0730", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"43ea71ec-d18c-4861-8ccb-bbf31aa7e373", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"79dec986-693c-4b69-8fb1-8c1e3a5b044c", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"e8165a83-0c9b-4724-a85e-89705b3b0557", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"9a0aee7f-ed6a-430b-a68a-c4e4c159c718", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"16a557a7-2c33-4377-a6b6-178ca8c717a8", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"bff6cafa-9881-4599-8c8f-ab5c7ebccb5f", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"0e09dc5c-d202-425a-b164-d505f631c2a5", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"46e46439-955b-406c-b95a-2e0ebfdb7dbb", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"1814969d-7e5a-4f03-bd89-d70b10944df3", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"2ddbb0c1-d5fd-4968-bc2a-efbab730adc7", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"94667b93-f74e-4d9c-9d94-510de66e922a", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"58fc84b0-b346-489e-94b4-af16e3700172", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"126a10a5-147b-44df-86a4-e284cc636241", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"9ccf2bce-177a-4aa3-bbf0-e2533a5bb761", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"e0360220-fa2f-4315-afb6-ea6ab6c31697", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"8dff5c77-57fc-475d-9c2f-6bae4803862f", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"7d7b7a31-a452-4ef7-b2e7-b75dd47900ef", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"e7ed0d93-762a-4e9f-b62f-35501cc1b7b1", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"05c652cb-9685-4f73-827e-7df33c7df2ab", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"6a7a2b0e-0267-4bf3-8d55-52d4bd968e4f", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"29edab7d-ae53-4143-a9a8-5fdeb37f6f06", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"230973a0-90f9-4e49-9bd9-654f4f84e93c", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"0a5611e9-6dbe-4dcc-af5f-a2a47e602003", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"c9e22bdb-bd5d-452a-98ae-59010f9c1e1f", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"8e67ee55-346e-49ad-b118-7ff9aab6ff7e", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"305b6095-cc32-4a52-98cf-231eb476999e", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"b18a0f04-6f7d-46c2-a6f9-ec7ed0536183", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"6ef8f831-e54e-4793-8356-c1e32a7a9085", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"0e05c8b3-7973-4137-bcc8-453394b9e7f2", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"b904a285-32b7-4981-b531-e52fe893f045", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"2d6b05f3-ee39-4ebd-a308-700fda6e8c31", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"fbfa7f46-106e-4ce5-ad06-21edb0fa0a2a", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"cd44e4fe-ba3f-4528-adb8-342332683d83", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"f8128971-f566-4e71-b138-2a60dc70b643", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"30526209-775b-4f1a-8b03-0f89480a41c7", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"f5f3357e-d17e-4f44-84d9-08eb9279df45", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"14cd87ca-324b-4efd-aa7a-5dba587b08a9", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"7013e698-5e8b-4c23-bf99-84ed90970494", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"687ee6c3-e2ae-4281-a636-dc6109a8198b", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"cc45ea72-f612-4eff-a54b-7b774c412654", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"bcb2a63d-ed18-40a1-85a5-5344d7483a22", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"f19014b2-2efc-445c-b7bf-e550c1d4d202", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"047ae985-e05a-4545-9604-01e66f7b5c30", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"9c769862-c2d0-4bb8-90ad-c53a13762d99", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"0e5f19fe-9851-479e-a293-a237f28e35cf", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"4803692e-b77f-41ed-9c94-fcdb679b5c46", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"7bb94047-a2e6-4abe-9c20-4d0a1b3ce02d", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"7a88aad3-96ec-4ce0-9387-055f4d16c458", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"df242914-b1e8-4884-9ccc-84222fb5e892", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"317dc222-d432-4cf9-be7b-e598e7c80df0", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"1b80381c-3f20-44c1-8d52-b8bfb4cabf56", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"bc03206b-8557-43c3-8787-44dc57962b41", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"1bd0c286-8518-4cd9-a9b9-dc8470ca2dbf", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"d5e1ae55-a3d1-4f27-9253-d380c60bddb0", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"5e14b03b-63d9-47e4-a620-eedc50c832c2", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"9944e4d7-0c65-4483-b4fc-ee1458eb2be9", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"e55b6c97-bd5c-47a8-9f93-1ad1db063d8a", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"d66021c5-dcd5-4641-8ef2-ef2d8249865b", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"8db9975b-c0a9-415e-9118-511e283d2406", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"c703d29d-44d9-4d92-820b-0551ed96ce67", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"28d3ce11-cfd8-45f5-9152-5eee8debbc4c", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"cd350c79-5a6f-4481-87c6-71b6a49508d8", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"7a0c0c7c-6f97-4a02-a1af-3eadb8b5da0d", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"d0281b31-ad96-406f-bac4-044c89c820d3", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"2a858f90-34ba-44f0-80d2-48ca357625e2", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"41ecff77-89a1-4fe5-8f0d-79f9b9c09e64", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"a420baf3-7806-4594-b56a-0c594de860cc", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"e854305d-ef10-4305-80c7-a74496419753", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"2f4bce09-d3ca-4c14-a133-ff6218b7cb99", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"8ce31325-c4b8-4004-ac0a-d5d90c0cf224", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"0eb22e04-34ad-4f26-bfca-867787806420", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"3559a22f-8c9b-4b58-b307-17370b7a9f7c", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"3a90381e-2aa5-4d5a-973b-240085a58810", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"e0577032-6542-4715-be55-50db6b8846d8", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"68d44507-9245-4e00-90bc-822f5a506b9e", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"94c0c04b-6ec8-4ef9-b008-b94425da4403", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"d08fb6e4-9b59-45c5-9ba2-bdc03cf97ae9", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"56a2ed14-fa9f-4b60-959e-94c34065501f", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"7c1a4353-a850-43f8-bbbe-0be449f85fd6", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"3568d901-2734-4122-b346-288e8dbfb076", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"e8f8f2f4-fcda-4f38-8139-6cba54350b3e", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"b11f7412-ba2f-43ea-bb5f-ca4911e789f8", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"e6a96315-7fbb-4c2d-8c83-b160fe7c9960", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"1c688ad7-e9a8-4dce-ab94-77faa0b20530", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0}
]},
{label: "r2", times: [
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"f48de4a4-ab2e-4ab9-9b0e-26c097899b62", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task A (Sim1)", task: "User Task A", "id":"5e206548-d99f-4bd3-8819-b0ea45df27f4", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task A (Sim2)", task: "User Task A", "id":"af789ed3-a81f-4853-afa2-b5e557c43193", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"4c34c3da-6684-45cf-a32f-5c05efa189ee", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task A (Sim4)", task: "User Task A", "id":"b73059ca-7c4f-4caf-8492-436499fa96f9", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task A (Sim5)", task: "User Task A", "id":"b1fa47dd-a65c-4be3-9aca-7f0c97dcec63", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"55933515-bec4-4ec9-9ca2-58de53c52c8d", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"4cf79cbc-54e0-42e0-82f7-80cb3f286cf0", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task A (Sim8)", task: "User Task A", "id":"3b35e048-2c22-41d0-b6b4-523045ca02aa", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task A (Sim9)", task: "User Task A", "id":"b6870de4-8e19-418e-b6e4-d41dd1718b9e", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"c7110cdb-b0eb-4944-8282-f476c3695ce2", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"5411bd12-ab67-491d-972c-d9ac2f8cfe31", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task A (Sim12)", task: "User Task A", "id":"3f603e2a-b56e-4c6c-b278-f641879d4f45", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"18a1e2fb-e3e0-4a8b-8579-b1066b922148", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task A (Sim14)", task: "User Task A", "id":"30607a0e-b31d-4b4b-a949-9e64e75a84fe", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"1cca92c2-9ba6-4722-8370-3bdc798a4f82", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task A (Sim16)", task: "User Task A", "id":"ee6f9138-39f5-4317-9ab5-b90330052f7f", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task A (Sim17)", task: "User Task A", "id":"0f596cfb-8110-4968-b244-f2590ccaa53a", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"480fab0d-97a0-45de-a640-e1af1617ac6e", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task A (Sim19)", task: "User Task A", "id":"28ff0ca7-b47d-4b27-8ed1-0e7715d2cf99", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"39fdb042-c138-48d9-be91-6c4df57b1c89", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"8fb9a12b-da57-4b56-9807-fe1d986d9bf3", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"a4e78dda-53bf-415b-83f0-f85dd8c37d35", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task A (Sim23)", task: "User Task A", "id":"4f6f32b2-c9f1-4e1b-9792-4807637d6b8a", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"27fbbe44-e7d9-4e2a-a705-1ce2d82230fe", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"c360bcd5-3e90-4437-8a7d-5e15b4ed1c16", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task A (Sim26)", task: "User Task A", "id":"0bd45a1d-1c77-4a55-af0e-717ca5ac405f", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task A (Sim27)", task: "User Task A", "id":"6ae0149e-e164-4403-9411-3f6fdcd2b0a6", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"01828f10-052f-4017-9d66-796f4f27589b", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"e7949c81-fc50-4e8d-9e90-7b5e56c4f2a1", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"5ac4b7b5-8d91-451e-b164-9c275ef8aa9e", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"5f609978-b295-4c29-aef2-5bf701d5ce56", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"c469f215-c865-4fc5-b644-06f2169d3725", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task A (Sim33)", task: "User Task A", "id":"d9b3d6ad-82c9-43fe-a255-c7684f2c5886", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task A (Sim34)", task: "User Task A", "id":"e2c73c00-992d-4be7-8497-e8c85428661a", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"31f44caf-e6f0-497a-a975-e6d0b3917661", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task A (Sim36)", task: "User Task A", "id":"2939d49d-9fa0-409a-868a-da4539c7a869", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"45f23325-5930-467f-8779-7a72a13e395f", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"0ee7e8f1-1baa-4ee4-9455-1a41271967b2", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"41dc1f7e-e3ab-4d90-9893-e5d928307608", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"0a83cfa3-6d76-4265-b8ae-e73ea834bb3e", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"2f9fe2ed-2fa3-48ca-a7f5-3bfe7a29c950", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"5b374e6e-42f8-45f6-bad1-38e023608c53", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"9c51c0be-efb9-4c85-9b4d-da40ea9785e1", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"5a63ed85-0a5a-482f-9f62-2a24e528ecf9", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"f573aa87-927d-4c02-a19c-32721e87f844", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"616484c9-0b7c-4df1-a3e6-148b8548775e", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"ed9f5c57-af21-4afd-b9fe-b76bded9d629", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"d020ee6f-f905-4284-b66f-5f0a9e6ff295", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"c34dd702-1fe0-49dc-aeda-6f248896fea0", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"5df99b26-1e06-4baa-9122-d38507ee26a4", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"4795a731-895a-4754-9fa2-3186b1e966ca", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"8b2ca950-3d24-4072-8516-cfdd485fa6b7", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task A (Sim53)", task: "User Task A", "id":"175bbdb9-8cb7-4ba1-8e72-1a9b15d19300", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task A (Sim54)", task: "User Task A", "id":"8f94006e-b370-4313-85cf-264f0a29edbb", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"7378c257-f17c-4567-a0f8-d6ffb5a558c5", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"bcfe0e30-9441-4f1e-8c30-18c36bac7f0f", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"90bd7d46-aaa7-430a-a84d-19b29d40a353", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"ac6c6709-8643-4b89-a090-519630a88768", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"08e6ea88-d596-4e97-b56e-a28beee181af", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task A (Sim60)", task: "User Task A", "id":"a32edbcd-9df7-4267-af00-ebd2096ed81f", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task A (Sim61)", task: "User Task A", "id":"de9cfad5-3040-4954-bc27-38db4f8e6c16", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task A (Sim62)", task: "User Task A", "id":"b10d4a23-a73c-424a-99d4-77249a14b08f", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"a46be789-5bd1-49c4-8079-a69cf7ef28fc", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"81420da5-fcb0-446a-aceb-d25d0960b95a", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"7384e700-c2a9-4789-b23b-0811e81fd845", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"9dcde483-88b6-40a9-9ab6-72381e946093", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"9098a1fa-62a5-4083-bfe5-a385f4f5a465", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"59c9e2a1-440e-435b-8a19-f459a6e0fc06", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"89c849a1-70f5-4354-bd2f-b8d5c052e449", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task A (Sim70)", task: "User Task A", "id":"e59af7ab-9a4b-4f3f-be60-98d82419ab27", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"ce74e839-e61c-4665-8501-84daee8e3742", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"fe0cfa25-c78b-4037-be52-a2d66020a0a8", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"426654f3-6c0d-434d-8a9b-22e0b279a635", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task A (Sim74)", task: "User Task A", "id":"5935dbd5-6f55-4749-ab63-a44eb8a534fd", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"47005354-7894-4ae9-b41f-04b77fae0892", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"a7d98426-2f2a-40ee-b670-67649321c249", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"49caf3a8-21ec-43d1-bd14-47724244c8ac", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"d138c516-bab4-4fe9-aebd-cace7b1dbcdd", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"a7a70154-6f62-47f4-97b5-3049dfc37f82", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"7c69f349-6ac5-4cce-a1e9-533183ef1151", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task A (Sim81)", task: "User Task A", "id":"b6b69c24-d1a3-4d95-8a1d-5a70ee71478f", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"16434196-1752-44f8-a002-ac5f395b742b", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"f99b3b6e-dc4f-4819-8564-3b03feb57e37", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"6978e65b-e49d-47e5-957f-ab247f9ce7f2", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task A (Sim85)", task: "User Task A", "id":"ddccd7a4-76f7-41b2-abe3-899ef4d62e8e", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"76d67566-c15d-4d7b-a2f2-119fffe54788", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"afe97068-6a99-44ae-a775-542c0fb9ca04", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"af3e1330-7ae5-4c71-8b99-f70c54b60b8c", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task A (Sim89)", task: "User Task A", "id":"c63ab581-c545-4fca-acea-1d5961b692c1", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task A (Sim90)", task: "User Task A", "id":"58d32aa6-618b-4208-a72c-a57f33376de0", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"7b0e7929-2507-4cde-9c6f-2ff5fdb0088e", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"31bb47d7-f9dc-47ef-9927-7c57eb5b2422", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"72729c93-5025-4d29-b176-d1bb6cde495e", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task A (Sim94)", task: "User Task A", "id":"a817bfdb-80bd-473c-bf78-644b17a2aa0c", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"26777000-36c3-4cc5-96de-685c255d1a3c", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"43fc4b30-fa1e-4a7c-8501-70b5127771b9", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task A (Sim97)", task: "User Task A", "id":"66177e3d-71a6-42be-98d4-b479fddf7227", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"7af66542-70a6-498b-91e7-6f9732df2046", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task A (Sim99)", task: "User Task A", "id":"d63f3367-5b9c-4e06-a95a-b21d04316df6", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0}
]},
{label: "r3", times: [
	{"label":"User Task B (Sim0)", task: "User Task B", "id":"acae6f72-c365-43dd-b4ae-7c38709133e7", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"aa995815-b60a-4ead-8f8d-2b80e194f646", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"313a1c55-00a8-4b20-9d84-9af26c457260", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"c6093b9a-6bf7-45d1-9dc1-2b4c0ad37cc5", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"eefc1400-c23f-499b-9a7c-e38187b1d320", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"8db52696-5a12-4588-8214-7ca22b078847", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"f99e04c3-e6b7-42f8-b0cb-47d59c5d2aba", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task B (Sim7)", task: "User Task B", "id":"50062799-0b54-404b-81df-a0f5beb7830a", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"e6da0740-3962-4376-b3cc-57bd4ff71348", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"9a5831e4-024f-4755-8e4b-280290d99489", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim10)", task: "User Task B", "id":"66e2b7bc-3dfd-4987-b459-644b83850ac5", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"25e45214-aac9-4858-a4b1-1ef8a6bed98f", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"c237d861-e891-44f3-ade0-1dfc4e71b4e6", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"aef9dac0-b604-4dbb-8fef-639b7505e70c", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"19cf0d35-273a-4133-bba9-4ee1f93152d8", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"43a2bef4-a263-4056-abc6-11d94653a0bc", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"63739d65-4ad5-4337-8e9c-7ad2368c3763", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"807a93b2-a791-4761-bbff-644cfdde23e9", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"e8f896aa-0232-4f3b-bb0c-d25f54d562b3", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"a6c151ad-47fc-4941-ba69-eec5228585ed", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"d97db3b9-d238-4a20-b734-6446b05692f0", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task B (Sim21)", task: "User Task B", "id":"9899469e-5bf4-44f5-a7c8-407865b5e245", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"62864343-11a1-4c12-b2e0-912e2fc1edc6", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"f44cb7ac-eabf-4ac0-a6f2-cd93138abe60", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task B (Sim24)", task: "User Task B", "id":"813ac280-56c2-40ae-adeb-6a711d1cb350", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task B (Sim25)", task: "User Task B", "id":"3ce890ac-5ae4-4a78-af04-10747bf03575", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"6e9fd568-007f-450c-a231-c7848db826fd", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"8929add9-8e36-45b7-95ad-a8fe378b2392", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task B (Sim28)", task: "User Task B", "id":"daacaf75-9b20-460a-bc70-fb1965ce6865", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"06d59b16-845c-4a69-ace0-dd2f381d0fde", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"57f31fb4-7d54-4acb-a70e-b1ac0ff4cbd9", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"9ba0b5eb-b357-4de4-a044-4e577eb18e71", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"399d5df1-7f9e-4936-8570-91f43221dfb4", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"9a912a3f-c673-42f7-bc3d-87e1e1014648", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"63cdde8f-38c5-461c-bee4-e9956d340598", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"df912ad7-c557-4484-b3ea-f752864cf5d6", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"e7044f40-b54d-4319-a30b-a19bd921f64c", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task B (Sim37)", task: "User Task B", "id":"5dd70c51-86c8-4c0a-8f35-31188187e33e", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"f17fa9f6-76d6-462f-907d-90ddf924dd02", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"c71ef085-40e0-4b95-ad44-509469390eea", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task B (Sim40)", task: "User Task B", "id":"ac232918-e8ac-4c2d-ba12-459b7753b993", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"1e1c30b9-113b-478f-a42e-faf5f80cdbad", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"8741689a-ceb2-49df-aac6-9faf07c5439e", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"f30db9a7-b505-4ab9-a2f5-40af7f8d2efa", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"7471bb37-b0bd-4d8a-bdbb-834fb1bc99f3", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"3195ecc2-1c53-4b76-b749-43dbae1193b9", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"5a149e1c-08dc-48b2-a001-2350b0e10dfa", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task B (Sim47)", task: "User Task B", "id":"7bec03ff-3058-4762-9166-850b7dd1cca7", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task B (Sim48)", task: "User Task B", "id":"cfd69771-04b7-48aa-b401-2b5dc8f97c07", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"97687b0e-677c-4fbb-bf51-3adbd0f73c02", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"52ceaa5b-3202-4feb-861e-82c6f82b6e20", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"f7c24d69-4df7-4069-86b0-b6be27f1c0d6", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"58321c17-726f-4af1-a8f7-e148f92cfd6d", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"4ea046bf-4ca8-4ed2-abe3-57e7bb5d1875", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"5b066aed-36bd-4776-8c0f-8e30c499f079", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"f02b577d-b97e-4554-80ec-3bcab56e9397", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task B (Sim56)", task: "User Task B", "id":"c8cecf66-6257-4e82-bc12-4fc4d37962ba", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"cc777da1-55d3-439c-b051-6e0e2dfa7148", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"455eb50a-7b78-4e62-bcc2-e500853d7f83", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task B (Sim59)", task: "User Task B", "id":"92109263-dc7a-4738-a964-3753b12caf98", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"6db9dd6c-e4a8-4efc-9f0e-03c0c9fd7b81", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"4f95edfe-58ae-48cd-921a-96c2dca98c60", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"acaeaf64-6f96-4e44-8034-e9abcf664628", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"90b40750-5cae-496b-b53a-e4368a960e17", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"c6197d98-bd11-41db-9a6c-bcf35dd5853a", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"e977de8f-6878-4f57-a43a-5fcc89278c74", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"d0fdf236-e7f4-4b42-b8d1-5777d8749a72", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"5d81819f-efef-4821-9749-3ae7c2f042fb", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"c09a6e6e-a78c-42e2-90d0-e6618781c759", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task B (Sim69)", task: "User Task B", "id":"08533314-c2fb-4031-817a-201a63cd6281", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"f90629de-fbf5-457d-b473-0d44e80237a0", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"53a19c53-9f61-44ec-abf3-0d23e245223a", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"3523b237-23fb-4684-b63b-706490553486", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task B (Sim73)", task: "User Task B", "id":"cfdf0abc-76f5-4bf3-b69f-427b27df6c16", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"606627ac-a748-4e1d-838d-edd013b4ec24", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"a620d56f-020c-4438-a299-505cfbe68780", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task B (Sim76)", task: "User Task B", "id":"119b4660-ea10-4d90-a0bc-f1275f3fd31b", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task B (Sim77)", task: "User Task B", "id":"9486d766-77de-4e89-9150-ea0070e9ec17", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"c781a0b1-b2e4-4624-bf2f-62756f1848fc", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"a034fc3f-4e82-4835-9dd5-736e65c60cf2", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"7f9faaf6-5e08-4606-ab12-895be9e9f39b", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"8207dcfd-1b3e-4655-a2c4-dd2d7a78d7c6", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim82)", task: "User Task B", "id":"ff56cda6-95d9-43ef-a6e2-3ce853e5f3be", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task B (Sim83)", task: "User Task B", "id":"3f13b14b-afcf-4280-98f8-9a9fe305d3f5", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"8240f91b-c293-41ed-8f50-f04ca8b400ef", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"1e8f782c-196c-4970-a8f4-c9b1940be9c6", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"fabc9ea1-72af-4be4-87f5-70d80d6f4667", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task B (Sim87)", task: "User Task B", "id":"6fd5f74e-344d-471e-a59d-db2e9dff0e2b", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"b06e80b5-abd4-4647-878c-df98d3001172", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"e0f4de6f-3116-4ee4-b752-f425313877ee", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"df146b01-a4e0-417c-858e-d605e2d8ab40", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"b3e1550b-50a0-4168-84d5-708ab4af89ac", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"64473ab1-50ab-41cf-b2d2-5696525a78b0", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"1e171974-1ecc-4913-ac3d-60b3fceb9e3e", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"441d2bbd-04d2-4f05-a9c7-6818c8c31dbd", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task B (Sim95)", task: "User Task B", "id":"87f45820-50ea-41b5-a140-f02c9387b8ab", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"3257609f-d7af-47e8-afe5-6021aae7c71d", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"1b400678-80a7-4e87-b68f-cbd23fc9c81d", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"9db452e0-ade0-4b35-8e1d-263a62a01017", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"89b861ef-8fb2-4974-a828-9b8d1d361022", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0}
]},
{label: "r4", times: [
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"940f7a4a-c865-4ba0-adc7-a5ede8c0eb2f", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"9bb3efa5-ee90-4448-92b0-905d2ca8780b", "starting_time": 30, "ending_time": 35, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"9d603ef5-dcd4-43a3-9338-87b20179dc21", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"71b248ae-375d-43bc-84d3-4aeb33cdca7c", "starting_time": 70, "ending_time": 75, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"caa913ca-6348-4931-ad92-fd7cb1724707", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"97b9598a-95f4-47a6-8639-c78c5fa08af1", "starting_time": 110, "ending_time": 115, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"2f524a35-17a6-425e-b651-4adad37aa964", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"e0b565d9-8f64-4042-a54d-5fa2d1e3da2d", "starting_time": 150, "ending_time": 155, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"dd1ec9b6-ca7b-4c21-b4f0-226e9dada5a0", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"fbc9f0fc-c268-4de3-a646-c4fb453d129a", "starting_time": 190, "ending_time": 195, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"2864b9e3-e014-44cb-b376-666e8b9243d4", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"e1e115ff-9024-400a-90b8-4040efd319a3", "starting_time": 230, "ending_time": 235, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"300bc374-e0fd-4436-a85f-4a219e01c19d", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"10277267-4e75-43ce-ae6a-c0babc29628f", "starting_time": 270, "ending_time": 275, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"57929a49-b322-43cd-b4ba-3332390a15e8", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"a3ce469d-0c6a-4ce8-a41d-88bbfeae164b", "starting_time": 310, "ending_time": 315, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"d4b95801-c98c-4b63-b0fb-441e2e8e8998", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"69c85b14-ff13-4597-9f6f-cdd22871e7cd", "starting_time": 350, "ending_time": 355, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"1adf9530-45ad-412e-9958-95af961f1490", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"9dd47748-960b-4616-960b-53c601ce5168", "starting_time": 390, "ending_time": 395, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"a68a9d45-a3aa-49d6-8bc7-8bc969767c96", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"8a21698f-018a-4b9d-9795-e7ddaf67914f", "starting_time": 430, "ending_time": 435, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"99160aae-4f54-4a9a-8f35-64170f8e2bd1", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"bddd0514-529b-4774-912d-7a81a23067b0", "starting_time": 470, "ending_time": 475, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"ebcc6b11-4532-43d9-a4b8-d47c5e431e3b", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"4278fd8f-d30d-4d39-b267-a3efdceb11e9", "starting_time": 510, "ending_time": 515, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"b5e502b2-d93e-46b3-982b-24caf0810fb7", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"811d18d6-2e37-430e-aa3b-5d9589ea4b0a", "starting_time": 550, "ending_time": 555, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"4580af95-a133-4b2b-83f1-582ab1b350af", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"8c54604e-7dfc-4b18-845f-a63ca88ee479", "starting_time": 590, "ending_time": 595, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"c83c0a7e-05a4-46b4-a4b3-f1d8ba8b6705", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"6a8bc4dd-d3b1-413b-bfd0-b05a91a3fada", "starting_time": 630, "ending_time": 635, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"69eaf16b-d7bb-493d-a6aa-728051af75a5", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"40580f28-3d36-4fd5-b29b-d1291759f85f", "starting_time": 670, "ending_time": 675, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"4aeff45e-a81b-4a0f-bebe-929ad78c3127", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"22929f5c-6d16-4b8f-b748-1251b5f68372", "starting_time": 710, "ending_time": 715, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"44076cba-e618-43da-a293-c0d42dba067b", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"460282b8-908a-4274-a8a8-99ffdf480b5e", "starting_time": 750, "ending_time": 755, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"6c944d44-0fb2-4210-8c4b-4b102b77588d", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"83ba5f3d-0fcd-4e04-a9a3-33655fbf0586", "starting_time": 790, "ending_time": 795, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"e118aaf7-0b26-4846-8f64-12f4c02f6763", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"4fd47330-029d-4655-90e9-8940800e02df", "starting_time": 830, "ending_time": 835, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"3442544e-eab2-4977-a9c9-7cf4976bcf06", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"db9d7684-2548-4885-b4db-60e245c01a3a", "starting_time": 870, "ending_time": 875, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"6f4c9abf-a712-4454-93aa-1edfc3015c6f", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"07bc5839-35b4-4e5a-8187-ff40698283b1", "starting_time": 910, "ending_time": 915, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"98af8dd0-6c52-47d7-9df9-ed5be0589b02", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"54ae15b2-333a-42ad-9659-b6f16ed1c84c", "starting_time": 950, "ending_time": 955, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"2bd73069-2288-48a7-bfa2-336a99f616fa", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"e7c207c2-00b4-4600-8f62-648e24c222c5", "starting_time": 990, "ending_time": 995, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"bd1aeac8-0ddd-4e14-8ff4-b39543188643", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"19fcd983-0da8-473c-b4e9-b027d7dab109", "starting_time": 1030, "ending_time": 1035, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"737f134b-a5b5-41ef-a123-e5cfe0f479ba", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"f76151ee-9f11-429b-a184-b6261017d4b5", "starting_time": 1070, "ending_time": 1075, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"26c47c7b-8f43-42e1-b27a-65df7b363658", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"1e78f3f1-8c1b-465e-b853-3b052fe1935e", "starting_time": 1110, "ending_time": 1115, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"f547452f-6357-4118-9f42-fd82d42b7049", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"e1c46161-2635-48f9-851a-aa0e15ee9dd5", "starting_time": 1150, "ending_time": 1155, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"ba668e12-5c7c-4087-91ac-fdfe73dfce19", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"3fd6d78a-b79c-4c31-a2b8-bd465f589db9", "starting_time": 1190, "ending_time": 1195, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"d861f689-4103-41c2-886f-06f763407fe1", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"327f088d-4693-4120-80a7-1b5aaf789c25", "starting_time": 1230, "ending_time": 1235, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"549a4e79-2ed8-4e5e-a5c4-bf85a248a524", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"587f6433-15c4-458d-a664-03400cdf9888", "starting_time": 1270, "ending_time": 1275, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"671a5d5e-078f-4c9f-99cd-1d16087133c7", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"aa654c5e-fe38-4269-8d8a-93a92f2d0f05", "starting_time": 1310, "ending_time": 1315, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"40761b2a-da60-47b7-82b1-16e2537b6ff9", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"19c37bca-00ba-4bf7-b48e-b30982c7e824", "starting_time": 1350, "ending_time": 1355, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"46f1bcc4-2bad-4cd2-a431-5211375a88e6", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"452d9604-665f-436e-b738-8321965fc88f", "starting_time": 1390, "ending_time": 1395, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"1a58c250-e717-48a0-8f35-b937ca58c501", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"83dacf4f-6400-4f52-a467-6422b5578dd6", "starting_time": 1430, "ending_time": 1435, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"7eddb05a-b84d-4652-9c6e-01ce9ece343d", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"7bbf45e1-9244-4cca-ac9b-4860c181f41f", "starting_time": 1470, "ending_time": 1475, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"f077f316-c26b-430f-ac32-b8308e8aeaf3", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"fbdef0d0-6709-48e6-aada-d74cbd95b5a7", "starting_time": 1510, "ending_time": 1515, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"ee781b11-bd54-4b29-b121-da0475f82305", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"a38ba503-d4e5-443d-a01e-f782d9dbe3e4", "starting_time": 1550, "ending_time": 1555, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"46756c7b-3bb5-4a34-a767-d052d9c30fa2", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"c53c7e88-dfbd-4f88-a176-03cd047c9e4a", "starting_time": 1590, "ending_time": 1595, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"90863f9e-5b2d-4f3c-b31e-729d4b0abf99", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"c7863b94-7e7e-4939-83fe-361339b31ce8", "starting_time": 1630, "ending_time": 1635, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"9f7111ee-ce92-4d8b-94e9-85227311dd65", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"0daf83de-fa7e-4ca5-af65-2a6e215d7025", "starting_time": 1670, "ending_time": 1675, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"bdc1db14-0a75-4021-bbed-747242b8770e", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"17b2abdd-1fb1-449b-ae01-6b81b912c532", "starting_time": 1710, "ending_time": 1715, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"9814b897-ba1f-46fb-b2cb-f30392614879", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"3d753198-a20c-49e7-b989-f279eb3d70fb", "starting_time": 1750, "ending_time": 1755, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"967f69e2-96a7-4da2-bdb2-a7a3a7b7578e", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"b343aa27-7a75-41c0-9c7c-f6229acb35cd", "starting_time": 1790, "ending_time": 1795, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"16be40e7-5d88-437d-bbb5-b0bd3be27e54", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"e10e14dc-3fd3-49ab-9f6e-9d4cd8bdce19", "starting_time": 1830, "ending_time": 1835, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"d52b08b0-b897-4a5e-9755-ff659d3552f5", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"c64e0268-2191-483c-8e06-5265c8c5c392", "starting_time": 1870, "ending_time": 1875, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"7f6f37d4-d5a3-49a9-9cf4-c817576c4c63", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"fdb34382-1343-4b60-9aca-1a2772a17230", "starting_time": 1910, "ending_time": 1915, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"a29965f5-feb3-4dcd-a3b6-861382aacf8e", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"6e43f979-1c79-4985-90dd-55f5b0622e12", "starting_time": 1950, "ending_time": 1955, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"0b84ea88-6eaf-4cfa-a319-399e23c59623", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"0ca7a880-c145-47dc-a923-cb83d08318df", "starting_time": 1990, "ending_time": 1995, delay: 0, cost: 0}
]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"fde78f37-d735-47d1-aed6-14c059bb306e", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task B (Sim0)", task: "User Task B", "id":"acae6f72-c365-43dd-b4ae-7c38709133e7", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"f48de4a4-ab2e-4ab9-9b0e-26c097899b62", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"940f7a4a-c865-4ba0-adc7-a5ede8c0eb2f", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"0f96e0b6-080c-4150-945e-4092acccf536", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task A (Sim1)", task: "User Task A", "id":"5e206548-d99f-4bd3-8819-b0ea45df27f4", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"aa995815-b60a-4ead-8f8d-2b80e194f646", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"9bb3efa5-ee90-4448-92b0-905d2ca8780b", "starting_time": 30, "ending_time": 35, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"b9d10f80-1c7b-4322-bf38-75ffd57f6895", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task B (Sim10)", task: "User Task B", "id":"66e2b7bc-3dfd-4987-b459-644b83850ac5", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"c7110cdb-b0eb-4944-8282-f476c3695ce2", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"2864b9e3-e014-44cb-b376-666e8b9243d4", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"f2889528-977b-4823-ae94-172432f70537", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"5411bd12-ab67-491d-972c-d9ac2f8cfe31", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"25e45214-aac9-4858-a4b1-1ef8a6bed98f", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"e1e115ff-9024-400a-90b8-4040efd319a3", "starting_time": 230, "ending_time": 235, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"bef34e9a-c4cb-4e93-9598-9108cff2a937", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"c237d861-e891-44f3-ade0-1dfc4e71b4e6", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task A (Sim12)", task: "User Task A", "id":"3f603e2a-b56e-4c6c-b278-f641879d4f45", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"300bc374-e0fd-4436-a85f-4a219e01c19d", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"5cf78d91-2d56-4389-92a8-78ee8be9d3e1", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"aef9dac0-b604-4dbb-8fef-639b7505e70c", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"18a1e2fb-e3e0-4a8b-8579-b1066b922148", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"10277267-4e75-43ce-ae6a-c0babc29628f", "starting_time": 270, "ending_time": 275, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"63db68fe-329d-403c-8ad8-5c9c2dae7fb6", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"19cf0d35-273a-4133-bba9-4ee1f93152d8", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task A (Sim14)", task: "User Task A", "id":"30607a0e-b31d-4b4b-a949-9e64e75a84fe", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"57929a49-b322-43cd-b4ba-3332390a15e8", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"f6b3ee01-9676-4180-9605-b37ed33c0730", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"1cca92c2-9ba6-4722-8370-3bdc798a4f82", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"43a2bef4-a263-4056-abc6-11d94653a0bc", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"a3ce469d-0c6a-4ce8-a41d-88bbfeae164b", "starting_time": 310, "ending_time": 315, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"43ea71ec-d18c-4861-8ccb-bbf31aa7e373", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task A (Sim16)", task: "User Task A", "id":"ee6f9138-39f5-4317-9ab5-b90330052f7f", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"63739d65-4ad5-4337-8e9c-7ad2368c3763", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"d4b95801-c98c-4b63-b0fb-441e2e8e8998", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"79dec986-693c-4b69-8fb1-8c1e3a5b044c", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"807a93b2-a791-4761-bbff-644cfdde23e9", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task A (Sim17)", task: "User Task A", "id":"0f596cfb-8110-4968-b244-f2590ccaa53a", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"69c85b14-ff13-4597-9f6f-cdd22871e7cd", "starting_time": 350, "ending_time": 355, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"e8165a83-0c9b-4724-a85e-89705b3b0557", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"480fab0d-97a0-45de-a640-e1af1617ac6e", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"e8f896aa-0232-4f3b-bb0c-d25f54d562b3", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"1adf9530-45ad-412e-9958-95af961f1490", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"9a0aee7f-ed6a-430b-a68a-c4e4c159c718", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"a6c151ad-47fc-4941-ba69-eec5228585ed", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task A (Sim19)", task: "User Task A", "id":"28ff0ca7-b47d-4b27-8ed1-0e7715d2cf99", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"9dd47748-960b-4616-960b-53c601ce5168", "starting_time": 390, "ending_time": 395, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"6ceaf7ea-ad30-43ac-ab8f-7c35c5197bdf", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"313a1c55-00a8-4b20-9d84-9af26c457260", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task A (Sim2)", task: "User Task A", "id":"af789ed3-a81f-4853-afa2-b5e557c43193", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"9d603ef5-dcd4-43a3-9338-87b20179dc21", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"16a557a7-2c33-4377-a6b6-178ca8c717a8", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"39fdb042-c138-48d9-be91-6c4df57b1c89", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"d97db3b9-d238-4a20-b734-6446b05692f0", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"a68a9d45-a3aa-49d6-8bc7-8bc969767c96", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"bff6cafa-9881-4599-8c8f-ab5c7ebccb5f", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"8fb9a12b-da57-4b56-9807-fe1d986d9bf3", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task B (Sim21)", task: "User Task B", "id":"9899469e-5bf4-44f5-a7c8-407865b5e245", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"8a21698f-018a-4b9d-9795-e7ddaf67914f", "starting_time": 430, "ending_time": 435, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"0e09dc5c-d202-425a-b164-d505f631c2a5", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"62864343-11a1-4c12-b2e0-912e2fc1edc6", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"a4e78dda-53bf-415b-83f0-f85dd8c37d35", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"99160aae-4f54-4a9a-8f35-64170f8e2bd1", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"46e46439-955b-406c-b95a-2e0ebfdb7dbb", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"f44cb7ac-eabf-4ac0-a6f2-cd93138abe60", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task A (Sim23)", task: "User Task A", "id":"4f6f32b2-c9f1-4e1b-9792-4807637d6b8a", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"bddd0514-529b-4774-912d-7a81a23067b0", "starting_time": 470, "ending_time": 475, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"1814969d-7e5a-4f03-bd89-d70b10944df3", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"27fbbe44-e7d9-4e2a-a705-1ce2d82230fe", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task B (Sim24)", task: "User Task B", "id":"813ac280-56c2-40ae-adeb-6a711d1cb350", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"ebcc6b11-4532-43d9-a4b8-d47c5e431e3b", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"2ddbb0c1-d5fd-4968-bc2a-efbab730adc7", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task B (Sim25)", task: "User Task B", "id":"3ce890ac-5ae4-4a78-af04-10747bf03575", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"c360bcd5-3e90-4437-8a7d-5e15b4ed1c16", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"4278fd8f-d30d-4d39-b267-a3efdceb11e9", "starting_time": 510, "ending_time": 515, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"94667b93-f74e-4d9c-9d94-510de66e922a", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task A (Sim26)", task: "User Task A", "id":"0bd45a1d-1c77-4a55-af0e-717ca5ac405f", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"6e9fd568-007f-450c-a231-c7848db826fd", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"b5e502b2-d93e-46b3-982b-24caf0810fb7", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"58fc84b0-b346-489e-94b4-af16e3700172", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task A (Sim27)", task: "User Task A", "id":"6ae0149e-e164-4403-9411-3f6fdcd2b0a6", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"8929add9-8e36-45b7-95ad-a8fe378b2392", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"811d18d6-2e37-430e-aa3b-5d9589ea4b0a", "starting_time": 550, "ending_time": 555, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"126a10a5-147b-44df-86a4-e284cc636241", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task B (Sim28)", task: "User Task B", "id":"daacaf75-9b20-460a-bc70-fb1965ce6865", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"01828f10-052f-4017-9d66-796f4f27589b", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"4580af95-a133-4b2b-83f1-582ab1b350af", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"9ccf2bce-177a-4aa3-bbf0-e2533a5bb761", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"06d59b16-845c-4a69-ace0-dd2f381d0fde", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"e7949c81-fc50-4e8d-9e90-7b5e56c4f2a1", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"8c54604e-7dfc-4b18-845f-a63ca88ee479", "starting_time": 590, "ending_time": 595, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"eaca1646-fe9a-4b44-b8a4-190712685215", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"4c34c3da-6684-45cf-a32f-5c05efa189ee", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"c6093b9a-6bf7-45d1-9dc1-2b4c0ad37cc5", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"71b248ae-375d-43bc-84d3-4aeb33cdca7c", "starting_time": 70, "ending_time": 75, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"e0360220-fa2f-4315-afb6-ea6ab6c31697", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"5ac4b7b5-8d91-451e-b164-9c275ef8aa9e", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"57f31fb4-7d54-4acb-a70e-b1ac0ff4cbd9", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"c83c0a7e-05a4-46b4-a4b3-f1d8ba8b6705", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"8dff5c77-57fc-475d-9c2f-6bae4803862f", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"9ba0b5eb-b357-4de4-a044-4e577eb18e71", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"5f609978-b295-4c29-aef2-5bf701d5ce56", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"6a8bc4dd-d3b1-413b-bfd0-b05a91a3fada", "starting_time": 630, "ending_time": 635, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"7d7b7a31-a452-4ef7-b2e7-b75dd47900ef", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"c469f215-c865-4fc5-b644-06f2169d3725", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"399d5df1-7f9e-4936-8570-91f43221dfb4", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"69eaf16b-d7bb-493d-a6aa-728051af75a5", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"e7ed0d93-762a-4e9f-b62f-35501cc1b7b1", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task A (Sim33)", task: "User Task A", "id":"d9b3d6ad-82c9-43fe-a255-c7684f2c5886", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"9a912a3f-c673-42f7-bc3d-87e1e1014648", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"40580f28-3d36-4fd5-b29b-d1291759f85f", "starting_time": 670, "ending_time": 675, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"05c652cb-9685-4f73-827e-7df33c7df2ab", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"63cdde8f-38c5-461c-bee4-e9956d340598", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task A (Sim34)", task: "User Task A", "id":"e2c73c00-992d-4be7-8497-e8c85428661a", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"4aeff45e-a81b-4a0f-bebe-929ad78c3127", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"6a7a2b0e-0267-4bf3-8d55-52d4bd968e4f", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"df912ad7-c557-4484-b3ea-f752864cf5d6", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"31f44caf-e6f0-497a-a975-e6d0b3917661", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"22929f5c-6d16-4b8f-b748-1251b5f68372", "starting_time": 710, "ending_time": 715, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"29edab7d-ae53-4143-a9a8-5fdeb37f6f06", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task A (Sim36)", task: "User Task A", "id":"2939d49d-9fa0-409a-868a-da4539c7a869", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"e7044f40-b54d-4319-a30b-a19bd921f64c", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"44076cba-e618-43da-a293-c0d42dba067b", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"230973a0-90f9-4e49-9bd9-654f4f84e93c", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task B (Sim37)", task: "User Task B", "id":"5dd70c51-86c8-4c0a-8f35-31188187e33e", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"45f23325-5930-467f-8779-7a72a13e395f", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"460282b8-908a-4274-a8a8-99ffdf480b5e", "starting_time": 750, "ending_time": 755, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"0a5611e9-6dbe-4dcc-af5f-a2a47e602003", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"0ee7e8f1-1baa-4ee4-9455-1a41271967b2", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"f17fa9f6-76d6-462f-907d-90ddf924dd02", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"6c944d44-0fb2-4210-8c4b-4b102b77588d", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"c9e22bdb-bd5d-452a-98ae-59010f9c1e1f", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"c71ef085-40e0-4b95-ad44-509469390eea", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"41dc1f7e-e3ab-4d90-9893-e5d928307608", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"83ba5f3d-0fcd-4e04-a9a3-33655fbf0586", "starting_time": 790, "ending_time": 795, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"ff9c3870-35e2-472e-afa4-17903b3fdf4e", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task A (Sim4)", task: "User Task A", "id":"b73059ca-7c4f-4caf-8492-436499fa96f9", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"eefc1400-c23f-499b-9a7c-e38187b1d320", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"caa913ca-6348-4931-ad92-fd7cb1724707", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"8e67ee55-346e-49ad-b118-7ff9aab6ff7e", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task B (Sim40)", task: "User Task B", "id":"ac232918-e8ac-4c2d-ba12-459b7753b993", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"0a83cfa3-6d76-4265-b8ae-e73ea834bb3e", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"e118aaf7-0b26-4846-8f64-12f4c02f6763", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"305b6095-cc32-4a52-98cf-231eb476999e", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"1e1c30b9-113b-478f-a42e-faf5f80cdbad", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"2f9fe2ed-2fa3-48ca-a7f5-3bfe7a29c950", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"4fd47330-029d-4655-90e9-8940800e02df", "starting_time": 830, "ending_time": 835, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"b18a0f04-6f7d-46c2-a6f9-ec7ed0536183", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"8741689a-ceb2-49df-aac6-9faf07c5439e", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"5b374e6e-42f8-45f6-bad1-38e023608c53", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"3442544e-eab2-4977-a9c9-7cf4976bcf06", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"6ef8f831-e54e-4793-8356-c1e32a7a9085", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"f30db9a7-b505-4ab9-a2f5-40af7f8d2efa", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"9c51c0be-efb9-4c85-9b4d-da40ea9785e1", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"db9d7684-2548-4885-b4db-60e245c01a3a", "starting_time": 870, "ending_time": 875, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"0e05c8b3-7973-4137-bcc8-453394b9e7f2", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"5a63ed85-0a5a-482f-9f62-2a24e528ecf9", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"7471bb37-b0bd-4d8a-bdbb-834fb1bc99f3", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"6f4c9abf-a712-4454-93aa-1edfc3015c6f", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"b904a285-32b7-4981-b531-e52fe893f045", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"3195ecc2-1c53-4b76-b749-43dbae1193b9", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"f573aa87-927d-4c02-a19c-32721e87f844", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"07bc5839-35b4-4e5a-8187-ff40698283b1", "starting_time": 910, "ending_time": 915, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"2d6b05f3-ee39-4ebd-a308-700fda6e8c31", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"616484c9-0b7c-4df1-a3e6-148b8548775e", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"5a149e1c-08dc-48b2-a001-2350b0e10dfa", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"98af8dd0-6c52-47d7-9df9-ed5be0589b02", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"fbfa7f46-106e-4ce5-ad06-21edb0fa0a2a", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"ed9f5c57-af21-4afd-b9fe-b76bded9d629", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task B (Sim47)", task: "User Task B", "id":"7bec03ff-3058-4762-9166-850b7dd1cca7", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"54ae15b2-333a-42ad-9659-b6f16ed1c84c", "starting_time": 950, "ending_time": 955, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"cd44e4fe-ba3f-4528-adb8-342332683d83", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"d020ee6f-f905-4284-b66f-5f0a9e6ff295", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task B (Sim48)", task: "User Task B", "id":"cfd69771-04b7-48aa-b401-2b5dc8f97c07", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"2bd73069-2288-48a7-bfa2-336a99f616fa", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"f8128971-f566-4e71-b138-2a60dc70b643", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"97687b0e-677c-4fbb-bf51-3adbd0f73c02", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"c34dd702-1fe0-49dc-aeda-6f248896fea0", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"e7c207c2-00b4-4600-8f62-648e24c222c5", "starting_time": 990, "ending_time": 995, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"e0c7b15f-c765-4a8a-967d-847e95610aa7", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"8db52696-5a12-4588-8214-7ca22b078847", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task A (Sim5)", task: "User Task A", "id":"b1fa47dd-a65c-4be3-9aca-7f0c97dcec63", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"97b9598a-95f4-47a6-8639-c78c5fa08af1", "starting_time": 110, "ending_time": 115, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"30526209-775b-4f1a-8b03-0f89480a41c7", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"5df99b26-1e06-4baa-9122-d38507ee26a4", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"52ceaa5b-3202-4feb-861e-82c6f82b6e20", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"bd1aeac8-0ddd-4e14-8ff4-b39543188643", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"f5f3357e-d17e-4f44-84d9-08eb9279df45", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"f7c24d69-4df7-4069-86b0-b6be27f1c0d6", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"4795a731-895a-4754-9fa2-3186b1e966ca", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"19fcd983-0da8-473c-b4e9-b027d7dab109", "starting_time": 1030, "ending_time": 1035, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"14cd87ca-324b-4efd-aa7a-5dba587b08a9", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"8b2ca950-3d24-4072-8516-cfdd485fa6b7", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"58321c17-726f-4af1-a8f7-e148f92cfd6d", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"737f134b-a5b5-41ef-a123-e5cfe0f479ba", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"7013e698-5e8b-4c23-bf99-84ed90970494", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"4ea046bf-4ca8-4ed2-abe3-57e7bb5d1875", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task A (Sim53)", task: "User Task A", "id":"175bbdb9-8cb7-4ba1-8e72-1a9b15d19300", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"f76151ee-9f11-429b-a184-b6261017d4b5", "starting_time": 1070, "ending_time": 1075, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"687ee6c3-e2ae-4281-a636-dc6109a8198b", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task A (Sim54)", task: "User Task A", "id":"8f94006e-b370-4313-85cf-264f0a29edbb", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"5b066aed-36bd-4776-8c0f-8e30c499f079", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"26c47c7b-8f43-42e1-b27a-65df7b363658", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"cc45ea72-f612-4eff-a54b-7b774c412654", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"f02b577d-b97e-4554-80ec-3bcab56e9397", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"7378c257-f17c-4567-a0f8-d6ffb5a558c5", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"1e78f3f1-8c1b-465e-b853-3b052fe1935e", "starting_time": 1110, "ending_time": 1115, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"bcb2a63d-ed18-40a1-85a5-5344d7483a22", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"bcfe0e30-9441-4f1e-8c30-18c36bac7f0f", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task B (Sim56)", task: "User Task B", "id":"c8cecf66-6257-4e82-bc12-4fc4d37962ba", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"f547452f-6357-4118-9f42-fd82d42b7049", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"f19014b2-2efc-445c-b7bf-e550c1d4d202", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"90bd7d46-aaa7-430a-a84d-19b29d40a353", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"cc777da1-55d3-439c-b051-6e0e2dfa7148", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"e1c46161-2635-48f9-851a-aa0e15ee9dd5", "starting_time": 1150, "ending_time": 1155, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"047ae985-e05a-4545-9604-01e66f7b5c30", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"455eb50a-7b78-4e62-bcc2-e500853d7f83", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"ac6c6709-8643-4b89-a090-519630a88768", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"ba668e12-5c7c-4087-91ac-fdfe73dfce19", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"9c769862-c2d0-4bb8-90ad-c53a13762d99", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"08e6ea88-d596-4e97-b56e-a28beee181af", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task B (Sim59)", task: "User Task B", "id":"92109263-dc7a-4738-a964-3753b12caf98", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"3fd6d78a-b79c-4c31-a2b8-bd465f589db9", "starting_time": 1190, "ending_time": 1195, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"d5b15670-6d87-439b-b770-d06ef109b562", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"55933515-bec4-4ec9-9ca2-58de53c52c8d", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"f99e04c3-e6b7-42f8-b0cb-47d59c5d2aba", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"2f524a35-17a6-425e-b651-4adad37aa964", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"0e5f19fe-9851-479e-a293-a237f28e35cf", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"6db9dd6c-e4a8-4efc-9f0e-03c0c9fd7b81", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task A (Sim60)", task: "User Task A", "id":"a32edbcd-9df7-4267-af00-ebd2096ed81f", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"d861f689-4103-41c2-886f-06f763407fe1", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"4803692e-b77f-41ed-9c94-fcdb679b5c46", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"4f95edfe-58ae-48cd-921a-96c2dca98c60", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task A (Sim61)", task: "User Task A", "id":"de9cfad5-3040-4954-bc27-38db4f8e6c16", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"327f088d-4693-4120-80a7-1b5aaf789c25", "starting_time": 1230, "ending_time": 1235, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"7bb94047-a2e6-4abe-9c20-4d0a1b3ce02d", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"acaeaf64-6f96-4e44-8034-e9abcf664628", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task A (Sim62)", task: "User Task A", "id":"b10d4a23-a73c-424a-99d4-77249a14b08f", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"549a4e79-2ed8-4e5e-a5c4-bf85a248a524", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"7a88aad3-96ec-4ce0-9387-055f4d16c458", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"90b40750-5cae-496b-b53a-e4368a960e17", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"a46be789-5bd1-49c4-8079-a69cf7ef28fc", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"587f6433-15c4-458d-a664-03400cdf9888", "starting_time": 1270, "ending_time": 1275, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"df242914-b1e8-4884-9ccc-84222fb5e892", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"c6197d98-bd11-41db-9a6c-bcf35dd5853a", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"81420da5-fcb0-446a-aceb-d25d0960b95a", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"671a5d5e-078f-4c9f-99cd-1d16087133c7", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"317dc222-d432-4cf9-be7b-e598e7c80df0", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"7384e700-c2a9-4789-b23b-0811e81fd845", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"e977de8f-6878-4f57-a43a-5fcc89278c74", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"aa654c5e-fe38-4269-8d8a-93a92f2d0f05", "starting_time": 1310, "ending_time": 1315, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"1b80381c-3f20-44c1-8d52-b8bfb4cabf56", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"d0fdf236-e7f4-4b42-b8d1-5777d8749a72", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"9dcde483-88b6-40a9-9ab6-72381e946093", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"40761b2a-da60-47b7-82b1-16e2537b6ff9", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"bc03206b-8557-43c3-8787-44dc57962b41", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"5d81819f-efef-4821-9749-3ae7c2f042fb", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"9098a1fa-62a5-4083-bfe5-a385f4f5a465", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"19c37bca-00ba-4bf7-b48e-b30982c7e824", "starting_time": 1350, "ending_time": 1355, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"1bd0c286-8518-4cd9-a9b9-dc8470ca2dbf", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"59c9e2a1-440e-435b-8a19-f459a6e0fc06", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"c09a6e6e-a78c-42e2-90d0-e6618781c759", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"46f1bcc4-2bad-4cd2-a431-5211375a88e6", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"d5e1ae55-a3d1-4f27-9253-d380c60bddb0", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task B (Sim69)", task: "User Task B", "id":"08533314-c2fb-4031-817a-201a63cd6281", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"89c849a1-70f5-4354-bd2f-b8d5c052e449", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"452d9604-665f-436e-b738-8321965fc88f", "starting_time": 1390, "ending_time": 1395, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"1acc4b94-c8f3-4791-8b65-956ea97fc410", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"4cf79cbc-54e0-42e0-82f7-80cb3f286cf0", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task B (Sim7)", task: "User Task B", "id":"50062799-0b54-404b-81df-a0f5beb7830a", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"e0b565d9-8f64-4042-a54d-5fa2d1e3da2d", "starting_time": 150, "ending_time": 155, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"5e14b03b-63d9-47e4-a620-eedc50c832c2", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"f90629de-fbf5-457d-b473-0d44e80237a0", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task A (Sim70)", task: "User Task A", "id":"e59af7ab-9a4b-4f3f-be60-98d82419ab27", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"1a58c250-e717-48a0-8f35-b937ca58c501", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"9944e4d7-0c65-4483-b4fc-ee1458eb2be9", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"ce74e839-e61c-4665-8501-84daee8e3742", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"53a19c53-9f61-44ec-abf3-0d23e245223a", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"83dacf4f-6400-4f52-a467-6422b5578dd6", "starting_time": 1430, "ending_time": 1435, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"e55b6c97-bd5c-47a8-9f93-1ad1db063d8a", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"fe0cfa25-c78b-4037-be52-a2d66020a0a8", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"3523b237-23fb-4684-b63b-706490553486", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"7eddb05a-b84d-4652-9c6e-01ce9ece343d", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"d66021c5-dcd5-4641-8ef2-ef2d8249865b", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"426654f3-6c0d-434d-8a9b-22e0b279a635", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task B (Sim73)", task: "User Task B", "id":"cfdf0abc-76f5-4bf3-b69f-427b27df6c16", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"7bbf45e1-9244-4cca-ac9b-4860c181f41f", "starting_time": 1470, "ending_time": 1475, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"8db9975b-c0a9-415e-9118-511e283d2406", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"606627ac-a748-4e1d-838d-edd013b4ec24", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task A (Sim74)", task: "User Task A", "id":"5935dbd5-6f55-4749-ab63-a44eb8a534fd", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"f077f316-c26b-430f-ac32-b8308e8aeaf3", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"c703d29d-44d9-4d92-820b-0551ed96ce67", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"a620d56f-020c-4438-a299-505cfbe68780", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"47005354-7894-4ae9-b41f-04b77fae0892", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"fbdef0d0-6709-48e6-aada-d74cbd95b5a7", "starting_time": 1510, "ending_time": 1515, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"28d3ce11-cfd8-45f5-9152-5eee8debbc4c", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task B (Sim76)", task: "User Task B", "id":"119b4660-ea10-4d90-a0bc-f1275f3fd31b", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"a7d98426-2f2a-40ee-b670-67649321c249", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"ee781b11-bd54-4b29-b121-da0475f82305", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"cd350c79-5a6f-4481-87c6-71b6a49508d8", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task B (Sim77)", task: "User Task B", "id":"9486d766-77de-4e89-9150-ea0070e9ec17", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"49caf3a8-21ec-43d1-bd14-47724244c8ac", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"a38ba503-d4e5-443d-a01e-f782d9dbe3e4", "starting_time": 1550, "ending_time": 1555, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"7a0c0c7c-6f97-4a02-a1af-3eadb8b5da0d", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"c781a0b1-b2e4-4624-bf2f-62756f1848fc", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"d138c516-bab4-4fe9-aebd-cace7b1dbcdd", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"46756c7b-3bb5-4a34-a767-d052d9c30fa2", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"d0281b31-ad96-406f-bac4-044c89c820d3", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"a7a70154-6f62-47f4-97b5-3049dfc37f82", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"a034fc3f-4e82-4835-9dd5-736e65c60cf2", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"c53c7e88-dfbd-4f88-a176-03cd047c9e4a", "starting_time": 1590, "ending_time": 1595, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"a8d7e6c3-e5d6-4700-89b7-529f3d2997d9", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task A (Sim8)", task: "User Task A", "id":"3b35e048-2c22-41d0-b6b4-523045ca02aa", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"e6da0740-3962-4376-b3cc-57bd4ff71348", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"dd1ec9b6-ca7b-4c21-b4f0-226e9dada5a0", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"2a858f90-34ba-44f0-80d2-48ca357625e2", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"7c69f349-6ac5-4cce-a1e9-533183ef1151", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"7f9faaf6-5e08-4606-ab12-895be9e9f39b", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"90863f9e-5b2d-4f3c-b31e-729d4b0abf99", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"41ecff77-89a1-4fe5-8f0d-79f9b9c09e64", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task A (Sim81)", task: "User Task A", "id":"b6b69c24-d1a3-4d95-8a1d-5a70ee71478f", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"8207dcfd-1b3e-4655-a2c4-dd2d7a78d7c6", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"c7863b94-7e7e-4939-83fe-361339b31ce8", "starting_time": 1630, "ending_time": 1635, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"a420baf3-7806-4594-b56a-0c594de860cc", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"16434196-1752-44f8-a002-ac5f395b742b", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task B (Sim82)", task: "User Task B", "id":"ff56cda6-95d9-43ef-a6e2-3ce853e5f3be", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"9f7111ee-ce92-4d8b-94e9-85227311dd65", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"e854305d-ef10-4305-80c7-a74496419753", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"f99b3b6e-dc4f-4819-8564-3b03feb57e37", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task B (Sim83)", task: "User Task B", "id":"3f13b14b-afcf-4280-98f8-9a9fe305d3f5", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"0daf83de-fa7e-4ca5-af65-2a6e215d7025", "starting_time": 1670, "ending_time": 1675, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"2f4bce09-d3ca-4c14-a133-ff6218b7cb99", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"8240f91b-c293-41ed-8f50-f04ca8b400ef", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"6978e65b-e49d-47e5-957f-ab247f9ce7f2", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"bdc1db14-0a75-4021-bbed-747242b8770e", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"8ce31325-c4b8-4004-ac0a-d5d90c0cf224", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task A (Sim85)", task: "User Task A", "id":"ddccd7a4-76f7-41b2-abe3-899ef4d62e8e", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"1e8f782c-196c-4970-a8f4-c9b1940be9c6", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"17b2abdd-1fb1-449b-ae01-6b81b912c532", "starting_time": 1710, "ending_time": 1715, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"0eb22e04-34ad-4f26-bfca-867787806420", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"76d67566-c15d-4d7b-a2f2-119fffe54788", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"fabc9ea1-72af-4be4-87f5-70d80d6f4667", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"9814b897-ba1f-46fb-b2cb-f30392614879", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"3559a22f-8c9b-4b58-b307-17370b7a9f7c", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task B (Sim87)", task: "User Task B", "id":"6fd5f74e-344d-471e-a59d-db2e9dff0e2b", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"afe97068-6a99-44ae-a775-542c0fb9ca04", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"3d753198-a20c-49e7-b989-f279eb3d70fb", "starting_time": 1750, "ending_time": 1755, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"3a90381e-2aa5-4d5a-973b-240085a58810", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"af3e1330-7ae5-4c71-8b99-f70c54b60b8c", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"b06e80b5-abd4-4647-878c-df98d3001172", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"967f69e2-96a7-4da2-bdb2-a7a3a7b7578e", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"e0577032-6542-4715-be55-50db6b8846d8", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"e0f4de6f-3116-4ee4-b752-f425313877ee", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task A (Sim89)", task: "User Task A", "id":"c63ab581-c545-4fca-acea-1d5961b692c1", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"b343aa27-7a75-41c0-9c7c-f6229acb35cd", "starting_time": 1790, "ending_time": 1795, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"470c4fed-53d8-43ac-8e83-b1ea6df22e39", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task A (Sim9)", task: "User Task A", "id":"b6870de4-8e19-418e-b6e4-d41dd1718b9e", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"9a5831e4-024f-4755-8e4b-280290d99489", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"fbc9f0fc-c268-4de3-a646-c4fb453d129a", "starting_time": 190, "ending_time": 195, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"68d44507-9245-4e00-90bc-822f5a506b9e", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"df146b01-a4e0-417c-858e-d605e2d8ab40", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task A (Sim90)", task: "User Task A", "id":"58d32aa6-618b-4208-a72c-a57f33376de0", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"16be40e7-5d88-437d-bbb5-b0bd3be27e54", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"94c0c04b-6ec8-4ef9-b008-b94425da4403", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"7b0e7929-2507-4cde-9c6f-2ff5fdb0088e", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"b3e1550b-50a0-4168-84d5-708ab4af89ac", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"e10e14dc-3fd3-49ab-9f6e-9d4cd8bdce19", "starting_time": 1830, "ending_time": 1835, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"d08fb6e4-9b59-45c5-9ba2-bdc03cf97ae9", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"31bb47d7-f9dc-47ef-9927-7c57eb5b2422", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"64473ab1-50ab-41cf-b2d2-5696525a78b0", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"d52b08b0-b897-4a5e-9755-ff659d3552f5", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"56a2ed14-fa9f-4b60-959e-94c34065501f", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"72729c93-5025-4d29-b176-d1bb6cde495e", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"1e171974-1ecc-4913-ac3d-60b3fceb9e3e", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"c64e0268-2191-483c-8e06-5265c8c5c392", "starting_time": 1870, "ending_time": 1875, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"7c1a4353-a850-43f8-bbbe-0be449f85fd6", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"441d2bbd-04d2-4f05-a9c7-6818c8c31dbd", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task A (Sim94)", task: "User Task A", "id":"a817bfdb-80bd-473c-bf78-644b17a2aa0c", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"7f6f37d4-d5a3-49a9-9cf4-c817576c4c63", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"3568d901-2734-4122-b346-288e8dbfb076", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task B (Sim95)", task: "User Task B", "id":"87f45820-50ea-41b5-a140-f02c9387b8ab", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"26777000-36c3-4cc5-96de-685c255d1a3c", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"fdb34382-1343-4b60-9aca-1a2772a17230", "starting_time": 1910, "ending_time": 1915, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"e8f8f2f4-fcda-4f38-8139-6cba54350b3e", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"43fc4b30-fa1e-4a7c-8501-70b5127771b9", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"3257609f-d7af-47e8-afe5-6021aae7c71d", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"a29965f5-feb3-4dcd-a3b6-861382aacf8e", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"b11f7412-ba2f-43ea-bb5f-ca4911e789f8", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"1b400678-80a7-4e87-b68f-cbd23fc9c81d", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task A (Sim97)", task: "User Task A", "id":"66177e3d-71a6-42be-98d4-b479fddf7227", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"6e43f979-1c79-4985-90dd-55f5b0622e12", "starting_time": 1950, "ending_time": 1955, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"e6a96315-7fbb-4c2d-8c83-b160fe7c9960", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"9db452e0-ade0-4b35-8e1d-263a62a01017", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"7af66542-70a6-498b-91e7-6f9732df2046", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"0b84ea88-6eaf-4cfa-a319-399e23c59623", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"1c688ad7-e9a8-4dce-ab94-77faa0b20530", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"89b861ef-8fb2-4974-a828-9b8d1d361022", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"User Task A (Sim99)", task: "User Task A", "id":"d63f3367-5b9c-4e06-a95a-b21d04316df6", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"0ca7a880-c145-47dc-a923-cb83d08318df", "starting_time": 1990, "ending_time": 1995, delay: 0, cost: 0}
]},
];
