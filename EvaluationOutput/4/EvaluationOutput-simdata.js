var tasks = [
	"Script Task",
	"Service Task",
	"User Task A",
	"User Task B",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"5c9a400e-7464-4394-ad53-13365391a361", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"b1062a0e-b974-440a-96b3-04b67a554c0d", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"0e8830b4-d3f4-4321-803a-cf9211a2c3b4", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"1ca146c5-5d6f-4c69-a87f-a17ebc75987f", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"d2cf9a63-d995-4ed4-8919-20843a16ab1e", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"59f8274c-acb9-4795-a5f8-6a2fc2915ec7", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"e8ddd716-6acc-40a4-8bb2-561560214cec", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"25d9ccb7-e432-484f-8eec-d6952757887e", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"006ec548-5396-40e1-8faf-a6849a38e549", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"49697610-876b-4c11-9d1f-352c7e4e7e53", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"c93c9103-8de4-49f0-a3a1-5265966b5007", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"5f6fecaa-c1c6-4f8d-863f-682a7fee3e55", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"baf7af7a-2b7a-4ab3-bc66-631e3d4a5110", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"5874abea-089c-4708-98e3-24b65f65bd02", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"e6a6e34b-8f6c-4dc0-be29-3af53fbb6aef", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"867941db-a75f-4b44-a112-28b673acce8c", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"ce63a571-25bf-49d0-ab6e-50661a646121", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"86fa64da-2450-4a3d-921d-d23d8e183d8c", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"de4088aa-209d-47f9-a11a-63fd11a0a2ea", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"cbcd9ee2-4b5a-47ba-b52f-1b2ae8df2941", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"1c7e3050-e451-4e0c-b525-01af5ffbd336", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"be45b763-9121-4fcc-bdd2-a393be7de0d0", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"726c76ee-f611-4673-a65b-37981f3014ec", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"5a594ff7-a570-4389-98ea-dc2f11f2404f", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"a4266234-f03c-46a4-889d-d67e5b5b1e44", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"64611469-c40d-40de-8f6c-6c4ca972138d", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"1331332a-bf2d-4c0a-aa6a-8cdab44b234e", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"3d606dcb-f825-4ffa-b880-fb731360bfbd", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"7fabdd26-9aa6-4182-b220-4d08b980b190", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"e347515f-10ee-4aab-96a7-4a2e5934afeb", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"2773bad6-84f5-467b-b638-bea4b55e4482", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"84a45fd1-de45-4fd5-a32e-73205095ffbd", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"e7e94646-94f0-4998-9013-7108e19c2f6e", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"00a7b7fa-d9f8-4870-adf6-8c1d0a35d9f9", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"1d72d641-1279-405f-bfff-48f02d6760b3", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"7028293b-436d-41c6-927d-c0dff20275b2", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"c836dab2-4840-44ca-b78d-c75ed17d41a4", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"cc619f32-0ee7-4a83-b646-4018e1d8029d", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"243225c6-aeda-43cb-9cef-8c0ca8593268", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"ab9d1fe5-a182-4cc8-8788-f652ab0af361", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"68ed4497-43b3-4ffa-96e8-e58a6c492ff5", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"8975d455-6472-427b-b797-171da6fa8ca1", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"26094b52-f3b4-44df-b206-36c9cd44a3a1", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"45c2a143-772e-496f-ad61-aa2293d99a8e", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"0d71a254-15d1-4455-85df-aa8a1a07567c", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"dc2367b3-1ea2-4282-827a-c059fc542a38", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"2e813bea-c5c0-446b-8e84-59270e279f04", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"51c7d2db-a9dc-4766-b125-f4058af5e135", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"1a5f3c8c-1557-48b0-800d-a0e2cc86676a", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"54a82e4f-58c1-49bc-9d68-f0881960ed16", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"4632b7eb-fec6-4590-a547-8b13b2f2eab0", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"5fa9c80d-7a62-4c6a-9d6b-ee8be4b9a4f2", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"0262460b-726a-4754-bcde-ac4f34fc99c1", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"f8754956-71a8-4c61-b6f1-144e8fee005d", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"30c1b8c4-2bfa-4a02-b2dc-cfa4ad755b17", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"ab6a5507-4f19-4cdf-b2c2-1004f42a5a56", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"3b226f1c-fb22-40b2-9dc5-3c6014ce9d86", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"f75178af-8e5a-4a69-b6ad-b16186b2bdf0", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"9d7f3d81-b250-48a6-84a4-4f7bf117529d", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"110dc9e1-4158-4863-a6d0-114fa0b23d1c", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"05bc6f0f-07cd-4f41-8bf5-f9d0b431f760", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"035b78c1-5b0f-4fd0-91a5-99d7073160d9", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"87eeb5af-4b91-4ff3-bfb5-d8e4960f7528", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"a500ab0e-873e-46da-b11c-4ddc5044c28a", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"f0c1cba5-e2b7-40b8-88de-c4b1ac7fda26", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"d0191f50-c5b8-4210-bce0-6461a8c9b933", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"f88cd2a3-322a-49a6-af92-bc7875681bce", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"84a9b50a-e4a2-4b33-a477-2ad4dee66097", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"40648cbf-7802-4e5e-935a-0983ab02df57", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"b26e293c-187c-45ca-bf26-302a402cb46b", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"ab7c28d2-f54a-42bc-851d-95409fbf3ad4", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"c0c6e8fe-a005-47a3-83f2-40c6475af8ea", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"6f9e435d-9141-4d92-b7ac-d288e2de4154", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"be1058a8-447e-4947-9429-f8dab529143c", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"100f6734-c3f6-43f6-ba9d-029f4a352659", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"c7e30991-2dd4-4487-b504-2660fd20c88f", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"16f37496-2758-48a9-a12b-6507511facbb", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"97bd8f43-daaa-4834-8183-97827157ec0b", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"3027667c-2647-4d69-b593-3ca3b0abc7b4", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"fdc7ded1-98e5-4bb4-9d26-2316316340ca", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"e55ff4e1-368b-4d0d-8974-e1ccf67892cc", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"58272965-7416-44dc-a1ac-dace00c4d1e1", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"7afb98c3-2984-4b10-886f-3d69ae7793d6", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"b4d703fb-7b5f-4ea1-ae6a-27527e4e01e7", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"e2e6b90d-3677-4a23-b92c-2083260abbd6", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"fae73964-9ae3-4f2a-a5a8-7567c9ccda9e", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"bce451e0-01a7-4887-a9cc-766028eef58a", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"bce9fd16-998c-4371-971f-d5f13b37fb83", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"c7c88a51-621c-4093-93fd-5a1714711946", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"bc595603-8091-4583-a150-cd4bd4af7ab1", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"ed0398d2-12eb-4abe-ab6b-1843d53dfb91", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"bf70bc88-76de-4050-b9ad-9905e4af3af2", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"1e3ec098-d374-42ca-a6b9-dc5ea256819a", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"bf281ff7-994e-4b45-96b7-48328f6736e5", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"d9bb48ca-a148-49f6-a63a-dca5d85a87cb", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"a3e138df-a3e3-4c9a-b21c-c5675414bd6c", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"9bb5de93-dda4-42c2-b43d-9c434becae89", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"88b238e2-7257-4ce1-ba66-16d5f41c0c26", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"6321ae93-b1aa-4b51-a9b2-77a6c1c7d8c5", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"5546a3e0-7fdd-4185-882a-85dd6fe434fc", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0}
]},
{label: "r2", times: [
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"44ae04a9-cff1-4dca-aa09-d047383a4aca", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task B (Sim0)", task: "User Task B", "id":"71eefb53-e4dc-45e1-a0bc-c8a68f45ae26", "starting_time": 10, "ending_time": 15, delay: 5, cost: 0},
	{"label":"User Task A (Sim1)", task: "User Task A", "id":"a6f088b2-331c-4c8e-87a0-d930f99d2aac", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"1fd05e41-1d45-4357-811a-c05ce4b0f3dd", "starting_time": 30, "ending_time": 35, delay: 5, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"d3fb2200-de27-479a-b37f-471ced69ae1f", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task A (Sim2)", task: "User Task A", "id":"433fa1d2-9403-4800-ba3b-3f66f2368e59", "starting_time": 50, "ending_time": 55, delay: 5, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"9c865f93-ff05-4842-8aa7-124268c9b6f4", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"7f3e37ec-41da-485f-8518-0be78952731a", "starting_time": 70, "ending_time": 75, delay: 5, cost: 0},
	{"label":"User Task A (Sim4)", task: "User Task A", "id":"a7e03f23-a206-4668-aff6-bcaae81f5a86", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"e71bd6ad-e57d-4804-9cbe-158c5cf65855", "starting_time": 90, "ending_time": 95, delay: 5, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"b1021573-d7e1-48c7-b851-ca60d77285a3", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task A (Sim5)", task: "User Task A", "id":"1e25cc0d-3ab9-498f-b810-7ae0fdc440c9", "starting_time": 110, "ending_time": 115, delay: 5, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"bb287dae-6bea-4205-ba04-07dc11a6a338", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"cc8c0c6a-d98d-4524-b7bb-5feebc9a7bef", "starting_time": 130, "ending_time": 135, delay: 5, cost: 0},
	{"label":"User Task B (Sim7)", task: "User Task B", "id":"e571b266-306a-47f4-8c44-3bfcd10a657e", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"7ac27f2f-5f57-4ea8-858e-0d9baa095d17", "starting_time": 150, "ending_time": 155, delay: 5, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"f6a59ea8-82cd-44a4-8a6d-d6e15169b90e", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task A (Sim8)", task: "User Task A", "id":"66211184-55da-4abe-95e3-121024d12ee8", "starting_time": 170, "ending_time": 175, delay: 5, cost: 0},
	{"label":"User Task A (Sim9)", task: "User Task A", "id":"e0402ad1-d5c6-4b46-a5db-9fd4db5bc1a8", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"297ab85a-2f93-4427-96b6-b7562f02aacd", "starting_time": 190, "ending_time": 195, delay: 5, cost: 0},
	{"label":"User Task B (Sim10)", task: "User Task B", "id":"27e06e5d-6520-4626-94ad-a1704a96a926", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"2b4cdc13-c506-4922-a7e2-87175fd857de", "starting_time": 210, "ending_time": 215, delay: 5, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"d281bb5e-0096-4df0-85aa-d7564d8e9d5a", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"f33fc158-b489-4405-84df-02a479d76bdd", "starting_time": 230, "ending_time": 235, delay: 5, cost: 0},
	{"label":"User Task A (Sim12)", task: "User Task A", "id":"d8a83c63-8763-4130-8d7e-1b120c181cd1", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"f258f245-eb7e-46fa-b805-fe49e58d3f39", "starting_time": 250, "ending_time": 255, delay: 5, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"a4c11e45-c6b6-420d-9a76-a94b995464c1", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"d7d53c4b-2240-46cd-980a-6a2bc04cd03f", "starting_time": 270, "ending_time": 275, delay: 5, cost: 0},
	{"label":"User Task A (Sim14)", task: "User Task A", "id":"a75a3ebf-8746-42c1-b3ce-d6866ea5f540", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"5d302816-0c78-495d-a8d1-8f207c29b198", "starting_time": 290, "ending_time": 295, delay: 5, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"0907e430-9245-4cf4-84d4-70b25c1944b6", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"6c3bb6b0-5ac3-4337-8288-b140f8f8c53b", "starting_time": 310, "ending_time": 315, delay: 5, cost: 0},
	{"label":"User Task A (Sim16)", task: "User Task A", "id":"12f8c04c-fbfa-4ef4-85bf-103b094fa65f", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"52c57949-34ba-42de-a5eb-346b683ea23a", "starting_time": 330, "ending_time": 335, delay: 5, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"d5bcf3fb-6d39-467f-aa3c-51d8fb71a19a", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task A (Sim17)", task: "User Task A", "id":"2301c828-aa3a-4231-a24f-b24bb738266a", "starting_time": 350, "ending_time": 355, delay: 5, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"f6aa3124-987f-4880-83a9-67e39cf41064", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"0d5e0941-e7ed-43fd-ba70-79b531712b38", "starting_time": 370, "ending_time": 375, delay: 5, cost: 0},
	{"label":"User Task A (Sim19)", task: "User Task A", "id":"1ce12608-1b89-4f60-8ac1-67d0bd092f4f", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"6bd05940-d111-4418-9679-f23a8b75c156", "starting_time": 390, "ending_time": 395, delay: 5, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"fc3d2855-ee2e-43ec-92c1-957a1fde9033", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"5193d36d-382b-4603-85c8-22f402323715", "starting_time": 410, "ending_time": 415, delay: 5, cost: 0},
	{"label":"User Task B (Sim21)", task: "User Task B", "id":"8322f634-9779-4c04-8948-3f8d4a053337", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"6f67735e-da29-45a6-a5b3-e67ebf7ec16d", "starting_time": 430, "ending_time": 435, delay: 5, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"bd03e39a-c446-4276-a525-05566ea0779c", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"d9e20e68-2379-4419-8a55-d8182c68842a", "starting_time": 450, "ending_time": 455, delay: 5, cost: 0},
	{"label":"User Task A (Sim23)", task: "User Task A", "id":"19a484bd-a919-424a-a07b-9f799777d9c7", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"7d05d9f3-2649-4064-80ea-7897e5cd701a", "starting_time": 470, "ending_time": 475, delay: 5, cost: 0},
	{"label":"User Task B (Sim24)", task: "User Task B", "id":"91153f97-d789-4eac-beb8-ee762475ec69", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"6ef18c88-21bf-49c6-910d-181bf5bef3fe", "starting_time": 490, "ending_time": 495, delay: 5, cost: 0},
	{"label":"User Task B (Sim25)", task: "User Task B", "id":"905a214d-5f9f-489f-9b23-5db9893e1031", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"51f243c7-0eff-4404-b04c-86e541a11d30", "starting_time": 510, "ending_time": 515, delay: 5, cost: 0},
	{"label":"User Task A (Sim26)", task: "User Task A", "id":"c6264cc5-66f8-499b-9780-498d9e72ce00", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"1f12c14a-d8fb-40ca-a717-63636f74a1d7", "starting_time": 530, "ending_time": 535, delay: 5, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"18bb0350-b576-4f76-94bd-2674ee0676b3", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task A (Sim27)", task: "User Task A", "id":"510f0d26-d9d2-466e-bcd7-7fd3e222a69c", "starting_time": 550, "ending_time": 555, delay: 5, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"b2dece4d-f7c7-48a0-ab9c-034d8d0cc9cd", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task B (Sim28)", task: "User Task B", "id":"4e72e17e-52ee-4db5-b794-856569c04758", "starting_time": 570, "ending_time": 575, delay: 5, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"b6cf463a-e864-43e1-8131-82906143638f", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"ebe0a6f1-f2fd-49e8-bd9d-dcd7f2281420", "starting_time": 590, "ending_time": 595, delay: 5, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"3bd839f9-1abe-401c-98bd-76b2ce0b042b", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"64542141-1d99-49dd-bfc5-6fdfbbfb27fa", "starting_time": 610, "ending_time": 615, delay: 5, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"2e7175ef-84ca-4a25-ae4c-567d093e7e55", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"522e8494-3f52-4df1-9961-af45e08dcfc4", "starting_time": 630, "ending_time": 635, delay: 5, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"3476598b-7b7a-4d66-a50b-c259ad5426e7", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"496ec310-5d35-4a57-b567-19070518ae61", "starting_time": 650, "ending_time": 655, delay: 5, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"f750003e-246c-4ff0-9abd-b8718d2377c1", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task A (Sim33)", task: "User Task A", "id":"67c84e49-623d-46c2-bea9-381c7978b75e", "starting_time": 670, "ending_time": 675, delay: 5, cost: 0},
	{"label":"User Task A (Sim34)", task: "User Task A", "id":"3ea90661-18f9-4c6e-a347-29ff2854ecc4", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"464cbfcb-acd4-4434-a08d-bf4c5e31192a", "starting_time": 690, "ending_time": 695, delay: 5, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"f1c75720-b63e-43b9-8b1e-b57ff2e4ffc2", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"0c937cc5-9e56-4453-ae63-86434d384de6", "starting_time": 710, "ending_time": 715, delay: 5, cost: 0},
	{"label":"User Task A (Sim36)", task: "User Task A", "id":"cf3c8f32-0247-468e-8752-9165fdb9c1fc", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"1797a858-6208-4a45-9191-8ab2fde26549", "starting_time": 730, "ending_time": 735, delay: 5, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"a12ea0fd-6315-48bf-9871-a35ceaf4faad", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task B (Sim37)", task: "User Task B", "id":"08f0ef10-0383-408d-8c93-d89f83013586", "starting_time": 750, "ending_time": 755, delay: 5, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"9fd84b5d-bdca-42a4-91b6-89403c503897", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"c81ced91-2b1a-4ebb-8e1f-20c82d5c24da", "starting_time": 770, "ending_time": 775, delay: 5, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"a9d9db83-6f90-4f0d-ac5f-1ffb8257a92e", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"456bea17-4c08-43f3-8a0d-058da4ea48d1", "starting_time": 790, "ending_time": 795, delay: 5, cost: 0},
	{"label":"User Task B (Sim40)", task: "User Task B", "id":"b9a1c200-6e3c-420f-8c06-c5fca5d65108", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"e336e63e-b9b0-4cfb-943d-0eec877f113b", "starting_time": 810, "ending_time": 815, delay: 5, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"eda11d6f-d24a-47f2-aa7e-2b8a5316c65c", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"1f3d10d0-b9cc-46bd-994f-591672ad4a37", "starting_time": 830, "ending_time": 835, delay: 5, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"17bd7bfa-d3f4-449d-bd88-cc92e6a1f92d", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"1fae9fc6-64c0-48ec-9abb-284fa064d3c6", "starting_time": 850, "ending_time": 855, delay: 5, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"318a731a-ff59-4d8e-a581-7c12e06d2c4b", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"716cc72f-2c70-4179-aa01-c75a00a48d37", "starting_time": 870, "ending_time": 875, delay: 5, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"36c18aa2-4466-4afd-b65f-c099f3dde9f1", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"7353c577-6169-4ca2-87c5-7f9c81f28279", "starting_time": 890, "ending_time": 895, delay: 5, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"82ba66ca-7438-4cdd-ba33-98409f47ed54", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"dc3e6b42-7afd-4704-8393-51b6c7f2c258", "starting_time": 910, "ending_time": 915, delay: 5, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"c8842b69-0104-4006-ab6d-1113c7d28eef", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"1bbd08c6-05db-4cd9-b69f-b058d7d35cdf", "starting_time": 930, "ending_time": 935, delay: 5, cost: 0},
	{"label":"User Task B (Sim47)", task: "User Task B", "id":"e7ff9549-ac16-48ef-9539-29479d729aac", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"5a9b3933-9413-4fb5-b04b-35274b983923", "starting_time": 950, "ending_time": 955, delay: 5, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"f253139a-6490-4fc5-aa9f-15d977633ba4", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task B (Sim48)", task: "User Task B", "id":"31d26f9a-fed9-4ccf-849e-157785cd4415", "starting_time": 970, "ending_time": 975, delay: 5, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"b01e85a3-4e87-4410-a2ac-8edb266cd7a4", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"362dd81d-7f93-47bc-abd7-39c7dab61f65", "starting_time": 990, "ending_time": 995, delay: 5, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"99be87f5-f2fb-4a23-b286-f667bd1045a4", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"9ab347b4-5d61-48a6-a710-8710a71e6233", "starting_time": 1010, "ending_time": 1015, delay: 5, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"039c24a2-5bcd-4485-9b44-bf9206855361", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"0ca20bbd-f974-43ae-8e4d-601a9a036e2a", "starting_time": 1030, "ending_time": 1035, delay: 5, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"da9041ea-3505-4766-9e7d-e5bd73aa30ff", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"4c3ba4f6-42ef-4c92-ae0e-2dfa1ec80f83", "starting_time": 1050, "ending_time": 1055, delay: 5, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"bfcbd4e6-e655-4e64-a8eb-c5b0d1c1bfb1", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task A (Sim53)", task: "User Task A", "id":"79e35b8d-2c07-417f-bc58-cb402078c204", "starting_time": 1070, "ending_time": 1075, delay: 5, cost: 0},
	{"label":"User Task A (Sim54)", task: "User Task A", "id":"17ff6e92-86dc-4aa4-9a22-e8fe62a5f845", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"370b153c-677d-4cfd-912e-1521ff41bab0", "starting_time": 1090, "ending_time": 1095, delay: 5, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"8de16d69-a6b8-4d97-80ab-b7ee2fc6a1ae", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"f3fb1e3a-146a-484c-b879-327874ae1853", "starting_time": 1110, "ending_time": 1115, delay: 5, cost: 0},
	{"label":"User Task B (Sim56)", task: "User Task B", "id":"ee89c5a3-8c56-4add-8bdb-e80432f89453", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"07f43a0a-fdf6-426b-adf8-8c56ae5bb30f", "starting_time": 1130, "ending_time": 1135, delay: 5, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"e2e7d487-eaf7-47f0-afd7-cc948f816a8a", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"17cd5ea2-41ae-41da-9ad2-c04d58b0a6cb", "starting_time": 1150, "ending_time": 1155, delay: 5, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"08e275c7-aa6a-4636-be54-72e6aced4715", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"7072e544-3e49-43db-9817-fd55610875a2", "starting_time": 1170, "ending_time": 1175, delay: 5, cost: 0},
	{"label":"User Task B (Sim59)", task: "User Task B", "id":"b770116f-0bc7-4d23-ac3f-cecca77bd451", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"f9e0ea9f-4e22-4695-bfdd-541ad9e2068b", "starting_time": 1190, "ending_time": 1195, delay: 5, cost: 0},
	{"label":"User Task A (Sim60)", task: "User Task A", "id":"8c683021-d8d2-448f-b066-4a4987901752", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"d232076e-220b-42c7-bc78-78f35e824535", "starting_time": 1210, "ending_time": 1215, delay: 5, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"2745c87c-fe51-4e34-9890-2211d27ff038", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task A (Sim61)", task: "User Task A", "id":"7492654b-6ac0-4b30-8403-65f446b24141", "starting_time": 1230, "ending_time": 1235, delay: 5, cost: 0},
	{"label":"User Task A (Sim62)", task: "User Task A", "id":"96c509f4-7fd8-46e3-b714-5186cc55544f", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"6ebfe2d5-c291-4a6d-a86c-eff1bc208565", "starting_time": 1250, "ending_time": 1255, delay: 5, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"168fe85e-30e5-474e-8be5-cf5c5fae5c32", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"739bcb4c-2365-47ee-aa8f-e4beee5b791b", "starting_time": 1270, "ending_time": 1275, delay: 5, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"267eca7e-acb8-41d2-94b6-20c1bfe62666", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"49d49cd4-7da8-4fc0-bea5-29f9d890e1e8", "starting_time": 1290, "ending_time": 1295, delay: 5, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"0e58437d-5caa-4a6e-84df-d2ec0d6f2b47", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"1a63e63a-2999-4ae9-991f-600ed199b4c6", "starting_time": 1310, "ending_time": 1315, delay: 5, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"81436282-b4c5-4242-aa71-36a2fc831de3", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"5ac24630-e53a-4d42-99d6-fdcc5c70b326", "starting_time": 1330, "ending_time": 1335, delay: 5, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"dff31545-72ef-4964-832f-9c7ee5c591eb", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"e6a4314f-4c49-495f-853b-715735fa9de6", "starting_time": 1350, "ending_time": 1355, delay: 5, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"cc7d27f4-9aeb-4be9-be0a-5803552aca22", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"21cc33a6-615d-4ad2-b312-d80ef3fb3d4a", "starting_time": 1370, "ending_time": 1375, delay: 5, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"34471685-546b-4ba4-b1c1-26f248f831d9", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task B (Sim69)", task: "User Task B", "id":"6194f423-bfdb-489c-980e-3fc7599f18db", "starting_time": 1390, "ending_time": 1395, delay: 5, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"71e5f0c9-4255-4b1f-89c1-792a3677fefd", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task A (Sim70)", task: "User Task A", "id":"7cfa78dc-84cf-488f-8ed7-309750a0c4f9", "starting_time": 1410, "ending_time": 1415, delay: 5, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"bc1bd018-0bda-42d4-994e-deefb9f03d22", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"35d49f38-133e-4c07-87f6-78321ebf5757", "starting_time": 1430, "ending_time": 1435, delay: 5, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"d077309e-4a93-4128-a2fa-05c1a3cbe019", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"27d3d4c0-56bd-4d6c-a2db-7109ebf83db6", "starting_time": 1450, "ending_time": 1455, delay: 5, cost: 0},
	{"label":"User Task B (Sim73)", task: "User Task B", "id":"85368850-dd16-41d9-a96f-eb79ff3ba382", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"3dfd33e4-c0fa-4b84-bb10-d9c1fd36f88c", "starting_time": 1470, "ending_time": 1475, delay: 5, cost: 0},
	{"label":"User Task A (Sim74)", task: "User Task A", "id":"d77951c5-3588-4e69-8f14-42d117fd9e48", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"0e2e993f-b680-4053-b6fb-79fe61c47e48", "starting_time": 1490, "ending_time": 1495, delay: 5, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"f1bc2bec-ed95-48d8-8867-aa60f0003649", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"f6f8bf06-9a30-4d34-b48a-4ce39d71698a", "starting_time": 1510, "ending_time": 1515, delay: 5, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"e590fa43-4c3d-4d2f-8a7e-d9e8a70c04be", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task B (Sim76)", task: "User Task B", "id":"f73866e0-1b3a-4e0a-ae51-58f0e6007443", "starting_time": 1530, "ending_time": 1535, delay: 5, cost: 0},
	{"label":"User Task B (Sim77)", task: "User Task B", "id":"d86f998e-cd80-4f54-b1ee-34881d6f6704", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"47ea21ed-a890-48a9-90eb-127a8fef61a4", "starting_time": 1550, "ending_time": 1555, delay: 5, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"b0e7ece7-c71c-420a-b4fc-18bca646ef86", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"117e65d8-7999-4298-aafb-e788bffa19b3", "starting_time": 1570, "ending_time": 1575, delay: 5, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"90209800-27e1-4096-854f-d447c152ce13", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"7b612738-81f4-4c92-9f4c-dfa6d28b6e10", "starting_time": 1590, "ending_time": 1595, delay: 5, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"edb26512-51b3-4fed-a40e-271ac1d10805", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"f1c37090-e412-4649-b06e-977cb28ebc54", "starting_time": 1610, "ending_time": 1615, delay: 5, cost: 0},
	{"label":"User Task A (Sim81)", task: "User Task A", "id":"863f9650-275b-4592-a5dc-7c48e16f9933", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"7972f9c8-9a11-4ed0-a6b2-efc1c0528314", "starting_time": 1630, "ending_time": 1635, delay: 5, cost: 0},
	{"label":"User Task B (Sim82)", task: "User Task B", "id":"e34f7f40-a441-46da-948f-2e1503500d98", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"3ba3460c-abf7-484c-a2d0-5e93c140c2a8", "starting_time": 1650, "ending_time": 1655, delay: 5, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"1e4f71dc-6dd8-4404-96fd-1fc08bfdbe82", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task B (Sim83)", task: "User Task B", "id":"4d957c6d-2834-41c0-b17d-6932ade8e27b", "starting_time": 1670, "ending_time": 1675, delay: 5, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"ab93a3bf-5e4e-493a-8d70-c00bb4002128", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"f0b85dcb-ca5e-47ae-bb2b-f7ad2ec63212", "starting_time": 1690, "ending_time": 1695, delay: 5, cost: 0},
	{"label":"User Task A (Sim85)", task: "User Task A", "id":"e5ba2f69-5962-4a4f-8ac4-93f87a7d347c", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"0606db8d-fa01-4b9a-8586-7888bb87bad7", "starting_time": 1710, "ending_time": 1715, delay: 5, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"c879a23d-7b6d-4362-beb8-a6cc3900073a", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"2363dc45-118e-4a91-81ee-7e786a596e11", "starting_time": 1730, "ending_time": 1735, delay: 5, cost: 0},
	{"label":"User Task B (Sim87)", task: "User Task B", "id":"06783bfb-55bb-434d-950f-f5726313ddae", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"5485c761-64f4-49e8-a393-1ff41c42bbdb", "starting_time": 1750, "ending_time": 1755, delay: 5, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"62c7eed9-780a-409e-af0f-1f9aabc7355f", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"71c2176e-53cb-42ea-89ea-40fb0d4202a6", "starting_time": 1770, "ending_time": 1775, delay: 5, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"e1d3e886-2dfc-4f8b-9578-00561b2d0af9", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task A (Sim89)", task: "User Task A", "id":"01b4bbee-8ad2-4356-b488-844cfdbc849d", "starting_time": 1790, "ending_time": 1795, delay: 5, cost: 0},
	{"label":"User Task A (Sim90)", task: "User Task A", "id":"860bf94a-aa4f-4603-ade2-3f9bd552f53d", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"9d06f2bb-62a4-4242-8637-7421f03d3d41", "starting_time": 1810, "ending_time": 1815, delay: 5, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"b7c1a97d-1cc5-4be6-9fee-50df165d2e02", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"6a739c67-7ef3-4324-b0e0-5a4178d9cee2", "starting_time": 1830, "ending_time": 1835, delay: 5, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"926b8d2c-e26d-4ae3-89b6-4bba5b004a99", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"ac8533f0-3630-4739-a6cd-6553d05089eb", "starting_time": 1850, "ending_time": 1855, delay: 5, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"8de635bf-403f-4267-a184-bcd117b1c17d", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"602329ae-3d95-423d-97cc-47654e092498", "starting_time": 1870, "ending_time": 1875, delay: 5, cost: 0},
	{"label":"User Task A (Sim94)", task: "User Task A", "id":"d7d72cb2-aa97-4116-be1a-bde0b7a21a0c", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"e83e3d8c-d5bc-4ea2-a6ef-5e76354e3a13", "starting_time": 1890, "ending_time": 1895, delay: 5, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"9ff6871e-3f9c-430f-8d50-b1b0c6b21f25", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task B (Sim95)", task: "User Task B", "id":"b1c16e50-8357-48fc-87a3-564892dddaa9", "starting_time": 1910, "ending_time": 1915, delay: 5, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"8b5f930e-7be3-4b03-bb5a-21018941f2ed", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"ed50f95d-7839-4e39-8836-bf28c413459d", "starting_time": 1930, "ending_time": 1935, delay: 5, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"3c9686c6-47ac-4954-bc13-3e838382249e", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task A (Sim97)", task: "User Task A", "id":"56584aa5-6396-498a-899f-08e080440c23", "starting_time": 1950, "ending_time": 1955, delay: 5, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"934d3303-a2db-45d5-969c-87d20ae318ba", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"d198fe3e-62cf-45a4-9e06-a893f2a2d516", "starting_time": 1970, "ending_time": 1975, delay: 5, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"deae4186-06e0-4028-bf24-527bcdc1640f", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"User Task A (Sim99)", task: "User Task A", "id":"eb02fe45-7717-496c-9b88-d17763565010", "starting_time": 1990, "ending_time": 1995, delay: 5, cost: 0}
]},
{label: "r3", times: [
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"36bbe59c-4ae7-412b-a386-14d0ae5aae25", "starting_time": 15, "ending_time": 20, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"3e10f940-501a-49a1-b307-93d50366dfe8", "starting_time": 35, "ending_time": 40, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"77f3bda5-aafa-4382-933d-9abeeb669ce7", "starting_time": 55, "ending_time": 60, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"995a6dbb-d3e4-40e6-b7c6-d975493ab1e9", "starting_time": 75, "ending_time": 80, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"527712b1-0c80-4fe0-91cb-50dce6cce814", "starting_time": 95, "ending_time": 100, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"153fb1e9-2111-4cb8-a0a4-68817ca5bf64", "starting_time": 115, "ending_time": 120, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"5e589e13-366e-4cc6-a480-f5e596186af3", "starting_time": 135, "ending_time": 140, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"4efd447c-350e-45d8-a8ae-92d559cd2097", "starting_time": 155, "ending_time": 160, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"732ce91a-daa1-4242-81ae-6f5f3033d68a", "starting_time": 175, "ending_time": 180, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"f84fc2d0-a8cf-4b3a-8f64-2dcdeecd46e5", "starting_time": 195, "ending_time": 200, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"aca47aa9-d037-40e8-b81c-22129d410ce9", "starting_time": 215, "ending_time": 220, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"59278f33-d6e2-4cc0-b07c-6710e22a322a", "starting_time": 235, "ending_time": 240, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"d914a430-ba44-4bb1-9616-9450a1e36423", "starting_time": 255, "ending_time": 260, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"4c89368e-7179-47e9-bbac-991942c6c285", "starting_time": 275, "ending_time": 280, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"fa46f6e8-215c-4003-92f4-8e647d20c066", "starting_time": 295, "ending_time": 300, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"63027ec5-3845-4f4d-9a61-528d1cbfff99", "starting_time": 315, "ending_time": 320, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"26fd4387-d97c-42af-8e30-f7ccc4c150d0", "starting_time": 335, "ending_time": 340, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"3f876c11-5059-4b8f-a699-d3f4f3f4556e", "starting_time": 355, "ending_time": 360, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"d7d8a9e2-5294-4c27-b7b3-5ad912ffdd06", "starting_time": 375, "ending_time": 380, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"70ac6d11-0d27-4f30-af59-d499c04048b5", "starting_time": 395, "ending_time": 400, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"24224ae1-0b7e-4cb0-bd3d-3c4e72f351e4", "starting_time": 415, "ending_time": 420, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"0c5a5b85-c07e-4564-8f46-d658f19dc542", "starting_time": 435, "ending_time": 440, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"e1f2cd73-6915-4a42-819f-d27fccb7b748", "starting_time": 455, "ending_time": 460, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"96960968-3496-4289-8406-a54966e51370", "starting_time": 475, "ending_time": 480, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"00b880e2-1fd0-4bed-9162-92f472ca4235", "starting_time": 495, "ending_time": 500, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"a3d51021-c0fb-4ad8-a659-1b9503b7917d", "starting_time": 515, "ending_time": 520, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"814cc803-5458-4c8a-b052-a2d34ebc9326", "starting_time": 535, "ending_time": 540, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"5b39dcc3-0dc9-4d4a-8738-db84d648e337", "starting_time": 555, "ending_time": 560, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"c42b24c2-dda9-4f69-bfd7-3514e2d6997b", "starting_time": 575, "ending_time": 580, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"042a0281-b55e-4352-9419-dd6d29328b89", "starting_time": 595, "ending_time": 600, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"90a32d83-c7fd-4ea1-a7d7-e1e845afd1a5", "starting_time": 615, "ending_time": 620, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"b2972ac5-461a-4d72-8d09-122877697236", "starting_time": 635, "ending_time": 640, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"da2d15a4-414f-417f-83a1-aa551329a26e", "starting_time": 655, "ending_time": 660, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"a7880c62-ec3b-4c31-a605-11bf0b6bbf81", "starting_time": 675, "ending_time": 680, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"6001d4ec-b835-45c1-b3b2-45fa8919291b", "starting_time": 695, "ending_time": 700, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"5e8acc8f-7c38-43f4-b53d-6ea82c185145", "starting_time": 715, "ending_time": 720, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"1c1166dd-cc40-4e14-8b83-928b1e0df103", "starting_time": 735, "ending_time": 740, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"cb344645-7bad-4c47-b6b4-da618b9fd788", "starting_time": 755, "ending_time": 760, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"059658dc-d338-48b9-9a35-03dec1f4ef5e", "starting_time": 775, "ending_time": 780, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"92d87687-976c-4913-b12e-cc49f9d08fd2", "starting_time": 795, "ending_time": 800, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"89488d3c-de5a-4ee2-a144-db950cdd1705", "starting_time": 815, "ending_time": 820, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"3ab8c36c-3b1f-4d76-94c1-46d023c662c0", "starting_time": 835, "ending_time": 840, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"0223b1fc-8043-4dfa-a1df-87729bfd3935", "starting_time": 855, "ending_time": 860, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"fef7ff7e-c0a5-4a29-b668-f6ec5b373352", "starting_time": 875, "ending_time": 880, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"d77f7053-0fae-48f8-afa4-ac95ef177531", "starting_time": 895, "ending_time": 900, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"cb3d2567-3f81-48d5-9222-80c6d6a63041", "starting_time": 915, "ending_time": 920, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"363bdb83-df37-4e8b-a825-82799d74ba5b", "starting_time": 935, "ending_time": 940, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"9475721d-6dec-4a07-84ea-ed29a840ec9e", "starting_time": 955, "ending_time": 960, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"2edbbf8a-c050-4f0e-bdb8-b7642fc84e6c", "starting_time": 975, "ending_time": 980, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"aac7f847-3c21-431b-ae28-93ca9428f80a", "starting_time": 995, "ending_time": 1000, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"abcf5c61-3f6e-4c99-a3c4-9dd8a6d9c1e2", "starting_time": 1015, "ending_time": 1020, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"d78c6568-b5a5-4af4-ad0a-83de7049d9db", "starting_time": 1035, "ending_time": 1040, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"422e91f2-4864-4daa-92e0-d1c56b076021", "starting_time": 1055, "ending_time": 1060, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"d298016a-47b5-4e65-9443-975279c52935", "starting_time": 1075, "ending_time": 1080, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"2cbbbb16-4923-4687-9cbb-3562d0b86498", "starting_time": 1095, "ending_time": 1100, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"b6353724-c0d0-42ac-9f35-64b45ab27156", "starting_time": 1115, "ending_time": 1120, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"1c28f9c8-3aeb-463e-b1c7-125e36f5b598", "starting_time": 1135, "ending_time": 1140, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"d3be5dd8-d8b2-489b-86bd-d654e37a2240", "starting_time": 1155, "ending_time": 1160, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"1820acd7-c78a-48a0-93e3-056e87d9cf4e", "starting_time": 1175, "ending_time": 1180, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"d2249e13-cc90-49f0-b82f-ec6c061568cd", "starting_time": 1195, "ending_time": 1200, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"df3c98ce-131f-41c9-8ed2-a4950432be62", "starting_time": 1215, "ending_time": 1220, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"d9e23cba-5960-41a3-b8b2-ea76d030f6f3", "starting_time": 1235, "ending_time": 1240, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"48e95f95-ce2f-4c54-bb93-f01161dd5539", "starting_time": 1255, "ending_time": 1260, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"43e0b8d3-7bda-4204-ac41-c026a38f03ce", "starting_time": 1275, "ending_time": 1280, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"1d077d1e-f17f-40f6-83a9-325b892cd709", "starting_time": 1295, "ending_time": 1300, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"b05b63d5-6de6-4d40-9515-f0f0b2238c8b", "starting_time": 1315, "ending_time": 1320, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"6ef06885-cac5-439f-8740-7284d6f761cf", "starting_time": 1335, "ending_time": 1340, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"9deb5706-025c-4f44-8d0e-7c9d3d6b443a", "starting_time": 1355, "ending_time": 1360, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"9d43bc6a-4435-4332-a590-0c161b939c84", "starting_time": 1375, "ending_time": 1380, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"a24877cc-827a-458e-bd57-f4dbe79d8660", "starting_time": 1395, "ending_time": 1400, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"5a44bfe2-4f6a-411a-bbe1-7ef8e234ddb4", "starting_time": 1415, "ending_time": 1420, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"a824b120-b90a-4dc2-a8ea-cb9e99d12b9d", "starting_time": 1435, "ending_time": 1440, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"ccca46c6-52da-4692-89b1-d3e6e5b968b8", "starting_time": 1455, "ending_time": 1460, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"b42f08a4-d7da-4982-9c06-576e2307b37e", "starting_time": 1475, "ending_time": 1480, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"b26892cb-0a9e-4e9d-99d4-a21e95fc327e", "starting_time": 1495, "ending_time": 1500, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"6136da08-f9fd-4bbd-b7a2-0702566077e8", "starting_time": 1515, "ending_time": 1520, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"2b1dee77-f9bd-4e30-acd0-aa9c5e9ed66f", "starting_time": 1535, "ending_time": 1540, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"ca3db503-bfb7-4790-a950-a74dcbe94bab", "starting_time": 1555, "ending_time": 1560, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"ae7b015d-e8b2-4006-872b-805f0d6a97d8", "starting_time": 1575, "ending_time": 1580, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"45705ebd-e466-4961-a845-47a1010da443", "starting_time": 1595, "ending_time": 1600, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"dcb4ac10-24df-435f-9767-dac89f146b64", "starting_time": 1615, "ending_time": 1620, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"af3d018f-f8b2-46c9-850e-6a39a20b8e50", "starting_time": 1635, "ending_time": 1640, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"d9e46271-acd1-49b8-97ec-39129ea070c0", "starting_time": 1655, "ending_time": 1660, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"e7f6f4d3-5cc2-4734-a07e-113df45f338c", "starting_time": 1675, "ending_time": 1680, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"544c23fe-89ea-45f2-bea6-09f3fac02b7f", "starting_time": 1695, "ending_time": 1700, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"8e8be4b8-5a1d-4a87-bf8c-8e06ee527dd8", "starting_time": 1715, "ending_time": 1720, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"948ebc1a-9d29-48b7-bd31-f39b6aef0068", "starting_time": 1735, "ending_time": 1740, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"f7626cbc-e04c-4d2c-a256-068919b548af", "starting_time": 1755, "ending_time": 1760, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"fc0c1d0b-86e9-43ea-968f-096330725ea7", "starting_time": 1775, "ending_time": 1780, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"c3436dfb-af86-44c9-827f-357dafc2c0ad", "starting_time": 1795, "ending_time": 1800, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"af7bee59-da18-41b8-8987-b7ac30780f97", "starting_time": 1815, "ending_time": 1820, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"f8926f40-8d79-41a4-a8f8-f136d74840f4", "starting_time": 1835, "ending_time": 1840, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"f9f7d413-5f6e-4670-83e9-13346a9229f3", "starting_time": 1855, "ending_time": 1860, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"9a8e6e47-03e7-4e6a-b963-991a4a5c8f4d", "starting_time": 1875, "ending_time": 1880, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"9d2f0fce-b86d-4a3f-9dad-e82d02f3c1cf", "starting_time": 1895, "ending_time": 1900, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"9abe1f4d-8c80-4060-a5cb-244c7120469e", "starting_time": 1915, "ending_time": 1920, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"9a402115-d28c-4f5b-b7f0-78847d3a8ae5", "starting_time": 1935, "ending_time": 1940, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"e346fb3b-58b6-42ce-b7ee-ae2548c22558", "starting_time": 1955, "ending_time": 1960, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"5dbf66d1-7896-4b8c-bbf5-1807b22d85ca", "starting_time": 1975, "ending_time": 1980, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"d0f496d7-991f-46e9-a943-14377be1f360", "starting_time": 1995, "ending_time": 2000, delay: 0, cost: 0}
]},
{label: "r4", times: [

]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"5c9a400e-7464-4394-ad53-13365391a361", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"44ae04a9-cff1-4dca-aa09-d047383a4aca", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task B (Sim0)", task: "User Task B", "id":"71eefb53-e4dc-45e1-a0bc-c8a68f45ae26", "starting_time": 10, "ending_time": 15, delay: 5, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"36bbe59c-4ae7-412b-a386-14d0ae5aae25", "starting_time": 15, "ending_time": 20, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"b1062a0e-b974-440a-96b3-04b67a554c0d", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task A (Sim1)", task: "User Task A", "id":"a6f088b2-331c-4c8e-87a0-d930f99d2aac", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"1fd05e41-1d45-4357-811a-c05ce4b0f3dd", "starting_time": 30, "ending_time": 35, delay: 5, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"3e10f940-501a-49a1-b307-93d50366dfe8", "starting_time": 35, "ending_time": 40, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"c93c9103-8de4-49f0-a3a1-5265966b5007", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task B (Sim10)", task: "User Task B", "id":"27e06e5d-6520-4626-94ad-a1704a96a926", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"2b4cdc13-c506-4922-a7e2-87175fd857de", "starting_time": 210, "ending_time": 215, delay: 5, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"aca47aa9-d037-40e8-b81c-22129d410ce9", "starting_time": 215, "ending_time": 220, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"5f6fecaa-c1c6-4f8d-863f-682a7fee3e55", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"d281bb5e-0096-4df0-85aa-d7564d8e9d5a", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"f33fc158-b489-4405-84df-02a479d76bdd", "starting_time": 230, "ending_time": 235, delay: 5, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"59278f33-d6e2-4cc0-b07c-6710e22a322a", "starting_time": 235, "ending_time": 240, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"baf7af7a-2b7a-4ab3-bc66-631e3d4a5110", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task A (Sim12)", task: "User Task A", "id":"d8a83c63-8763-4130-8d7e-1b120c181cd1", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"f258f245-eb7e-46fa-b805-fe49e58d3f39", "starting_time": 250, "ending_time": 255, delay: 5, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"d914a430-ba44-4bb1-9616-9450a1e36423", "starting_time": 255, "ending_time": 260, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"5874abea-089c-4708-98e3-24b65f65bd02", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"a4c11e45-c6b6-420d-9a76-a94b995464c1", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"d7d53c4b-2240-46cd-980a-6a2bc04cd03f", "starting_time": 270, "ending_time": 275, delay: 5, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"4c89368e-7179-47e9-bbac-991942c6c285", "starting_time": 275, "ending_time": 280, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"e6a6e34b-8f6c-4dc0-be29-3af53fbb6aef", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task A (Sim14)", task: "User Task A", "id":"a75a3ebf-8746-42c1-b3ce-d6866ea5f540", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"5d302816-0c78-495d-a8d1-8f207c29b198", "starting_time": 290, "ending_time": 295, delay: 5, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"fa46f6e8-215c-4003-92f4-8e647d20c066", "starting_time": 295, "ending_time": 300, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"867941db-a75f-4b44-a112-28b673acce8c", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"0907e430-9245-4cf4-84d4-70b25c1944b6", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"6c3bb6b0-5ac3-4337-8288-b140f8f8c53b", "starting_time": 310, "ending_time": 315, delay: 5, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"63027ec5-3845-4f4d-9a61-528d1cbfff99", "starting_time": 315, "ending_time": 320, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"ce63a571-25bf-49d0-ab6e-50661a646121", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task A (Sim16)", task: "User Task A", "id":"12f8c04c-fbfa-4ef4-85bf-103b094fa65f", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"52c57949-34ba-42de-a5eb-346b683ea23a", "starting_time": 330, "ending_time": 335, delay: 5, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"26fd4387-d97c-42af-8e30-f7ccc4c150d0", "starting_time": 335, "ending_time": 340, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"86fa64da-2450-4a3d-921d-d23d8e183d8c", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"d5bcf3fb-6d39-467f-aa3c-51d8fb71a19a", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task A (Sim17)", task: "User Task A", "id":"2301c828-aa3a-4231-a24f-b24bb738266a", "starting_time": 350, "ending_time": 355, delay: 5, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"3f876c11-5059-4b8f-a699-d3f4f3f4556e", "starting_time": 355, "ending_time": 360, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"de4088aa-209d-47f9-a11a-63fd11a0a2ea", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"f6aa3124-987f-4880-83a9-67e39cf41064", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"0d5e0941-e7ed-43fd-ba70-79b531712b38", "starting_time": 370, "ending_time": 375, delay: 5, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"d7d8a9e2-5294-4c27-b7b3-5ad912ffdd06", "starting_time": 375, "ending_time": 380, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"cbcd9ee2-4b5a-47ba-b52f-1b2ae8df2941", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task A (Sim19)", task: "User Task A", "id":"1ce12608-1b89-4f60-8ac1-67d0bd092f4f", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"6bd05940-d111-4418-9679-f23a8b75c156", "starting_time": 390, "ending_time": 395, delay: 5, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"70ac6d11-0d27-4f30-af59-d499c04048b5", "starting_time": 395, "ending_time": 400, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"0e8830b4-d3f4-4321-803a-cf9211a2c3b4", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"d3fb2200-de27-479a-b37f-471ced69ae1f", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task A (Sim2)", task: "User Task A", "id":"433fa1d2-9403-4800-ba3b-3f66f2368e59", "starting_time": 50, "ending_time": 55, delay: 5, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"77f3bda5-aafa-4382-933d-9abeeb669ce7", "starting_time": 55, "ending_time": 60, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"1c7e3050-e451-4e0c-b525-01af5ffbd336", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"fc3d2855-ee2e-43ec-92c1-957a1fde9033", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"5193d36d-382b-4603-85c8-22f402323715", "starting_time": 410, "ending_time": 415, delay: 5, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"24224ae1-0b7e-4cb0-bd3d-3c4e72f351e4", "starting_time": 415, "ending_time": 420, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"be45b763-9121-4fcc-bdd2-a393be7de0d0", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task B (Sim21)", task: "User Task B", "id":"8322f634-9779-4c04-8948-3f8d4a053337", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"6f67735e-da29-45a6-a5b3-e67ebf7ec16d", "starting_time": 430, "ending_time": 435, delay: 5, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"0c5a5b85-c07e-4564-8f46-d658f19dc542", "starting_time": 435, "ending_time": 440, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"726c76ee-f611-4673-a65b-37981f3014ec", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"bd03e39a-c446-4276-a525-05566ea0779c", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"d9e20e68-2379-4419-8a55-d8182c68842a", "starting_time": 450, "ending_time": 455, delay: 5, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"e1f2cd73-6915-4a42-819f-d27fccb7b748", "starting_time": 455, "ending_time": 460, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"5a594ff7-a570-4389-98ea-dc2f11f2404f", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task A (Sim23)", task: "User Task A", "id":"19a484bd-a919-424a-a07b-9f799777d9c7", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"7d05d9f3-2649-4064-80ea-7897e5cd701a", "starting_time": 470, "ending_time": 475, delay: 5, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"96960968-3496-4289-8406-a54966e51370", "starting_time": 475, "ending_time": 480, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"a4266234-f03c-46a4-889d-d67e5b5b1e44", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task B (Sim24)", task: "User Task B", "id":"91153f97-d789-4eac-beb8-ee762475ec69", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"6ef18c88-21bf-49c6-910d-181bf5bef3fe", "starting_time": 490, "ending_time": 495, delay: 5, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"00b880e2-1fd0-4bed-9162-92f472ca4235", "starting_time": 495, "ending_time": 500, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"64611469-c40d-40de-8f6c-6c4ca972138d", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task B (Sim25)", task: "User Task B", "id":"905a214d-5f9f-489f-9b23-5db9893e1031", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"51f243c7-0eff-4404-b04c-86e541a11d30", "starting_time": 510, "ending_time": 515, delay: 5, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"a3d51021-c0fb-4ad8-a659-1b9503b7917d", "starting_time": 515, "ending_time": 520, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"1331332a-bf2d-4c0a-aa6a-8cdab44b234e", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task A (Sim26)", task: "User Task A", "id":"c6264cc5-66f8-499b-9780-498d9e72ce00", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"1f12c14a-d8fb-40ca-a717-63636f74a1d7", "starting_time": 530, "ending_time": 535, delay: 5, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"814cc803-5458-4c8a-b052-a2d34ebc9326", "starting_time": 535, "ending_time": 540, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"3d606dcb-f825-4ffa-b880-fb731360bfbd", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"18bb0350-b576-4f76-94bd-2674ee0676b3", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task A (Sim27)", task: "User Task A", "id":"510f0d26-d9d2-466e-bcd7-7fd3e222a69c", "starting_time": 550, "ending_time": 555, delay: 5, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"5b39dcc3-0dc9-4d4a-8738-db84d648e337", "starting_time": 555, "ending_time": 560, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"7fabdd26-9aa6-4182-b220-4d08b980b190", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"b2dece4d-f7c7-48a0-ab9c-034d8d0cc9cd", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task B (Sim28)", task: "User Task B", "id":"4e72e17e-52ee-4db5-b794-856569c04758", "starting_time": 570, "ending_time": 575, delay: 5, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"c42b24c2-dda9-4f69-bfd7-3514e2d6997b", "starting_time": 575, "ending_time": 580, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"e347515f-10ee-4aab-96a7-4a2e5934afeb", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"b6cf463a-e864-43e1-8131-82906143638f", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"ebe0a6f1-f2fd-49e8-bd9d-dcd7f2281420", "starting_time": 590, "ending_time": 595, delay: 5, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"042a0281-b55e-4352-9419-dd6d29328b89", "starting_time": 595, "ending_time": 600, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"1ca146c5-5d6f-4c69-a87f-a17ebc75987f", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"9c865f93-ff05-4842-8aa7-124268c9b6f4", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"7f3e37ec-41da-485f-8518-0be78952731a", "starting_time": 70, "ending_time": 75, delay: 5, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"995a6dbb-d3e4-40e6-b7c6-d975493ab1e9", "starting_time": 75, "ending_time": 80, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"2773bad6-84f5-467b-b638-bea4b55e4482", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"3bd839f9-1abe-401c-98bd-76b2ce0b042b", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"64542141-1d99-49dd-bfc5-6fdfbbfb27fa", "starting_time": 610, "ending_time": 615, delay: 5, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"90a32d83-c7fd-4ea1-a7d7-e1e845afd1a5", "starting_time": 615, "ending_time": 620, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"84a45fd1-de45-4fd5-a32e-73205095ffbd", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"2e7175ef-84ca-4a25-ae4c-567d093e7e55", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"522e8494-3f52-4df1-9961-af45e08dcfc4", "starting_time": 630, "ending_time": 635, delay: 5, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"b2972ac5-461a-4d72-8d09-122877697236", "starting_time": 635, "ending_time": 640, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"e7e94646-94f0-4998-9013-7108e19c2f6e", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"3476598b-7b7a-4d66-a50b-c259ad5426e7", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"496ec310-5d35-4a57-b567-19070518ae61", "starting_time": 650, "ending_time": 655, delay: 5, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"da2d15a4-414f-417f-83a1-aa551329a26e", "starting_time": 655, "ending_time": 660, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"00a7b7fa-d9f8-4870-adf6-8c1d0a35d9f9", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"f750003e-246c-4ff0-9abd-b8718d2377c1", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task A (Sim33)", task: "User Task A", "id":"67c84e49-623d-46c2-bea9-381c7978b75e", "starting_time": 670, "ending_time": 675, delay: 5, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"a7880c62-ec3b-4c31-a605-11bf0b6bbf81", "starting_time": 675, "ending_time": 680, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"1d72d641-1279-405f-bfff-48f02d6760b3", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task A (Sim34)", task: "User Task A", "id":"3ea90661-18f9-4c6e-a347-29ff2854ecc4", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"464cbfcb-acd4-4434-a08d-bf4c5e31192a", "starting_time": 690, "ending_time": 695, delay: 5, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"6001d4ec-b835-45c1-b3b2-45fa8919291b", "starting_time": 695, "ending_time": 700, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"7028293b-436d-41c6-927d-c0dff20275b2", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"f1c75720-b63e-43b9-8b1e-b57ff2e4ffc2", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"0c937cc5-9e56-4453-ae63-86434d384de6", "starting_time": 710, "ending_time": 715, delay: 5, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"5e8acc8f-7c38-43f4-b53d-6ea82c185145", "starting_time": 715, "ending_time": 720, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"c836dab2-4840-44ca-b78d-c75ed17d41a4", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task A (Sim36)", task: "User Task A", "id":"cf3c8f32-0247-468e-8752-9165fdb9c1fc", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"1797a858-6208-4a45-9191-8ab2fde26549", "starting_time": 730, "ending_time": 735, delay: 5, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"1c1166dd-cc40-4e14-8b83-928b1e0df103", "starting_time": 735, "ending_time": 740, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"cc619f32-0ee7-4a83-b646-4018e1d8029d", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"a12ea0fd-6315-48bf-9871-a35ceaf4faad", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task B (Sim37)", task: "User Task B", "id":"08f0ef10-0383-408d-8c93-d89f83013586", "starting_time": 750, "ending_time": 755, delay: 5, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"cb344645-7bad-4c47-b6b4-da618b9fd788", "starting_time": 755, "ending_time": 760, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"243225c6-aeda-43cb-9cef-8c0ca8593268", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"9fd84b5d-bdca-42a4-91b6-89403c503897", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"c81ced91-2b1a-4ebb-8e1f-20c82d5c24da", "starting_time": 770, "ending_time": 775, delay: 5, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"059658dc-d338-48b9-9a35-03dec1f4ef5e", "starting_time": 775, "ending_time": 780, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"ab9d1fe5-a182-4cc8-8788-f652ab0af361", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"a9d9db83-6f90-4f0d-ac5f-1ffb8257a92e", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"456bea17-4c08-43f3-8a0d-058da4ea48d1", "starting_time": 790, "ending_time": 795, delay: 5, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"92d87687-976c-4913-b12e-cc49f9d08fd2", "starting_time": 795, "ending_time": 800, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"d2cf9a63-d995-4ed4-8919-20843a16ab1e", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task A (Sim4)", task: "User Task A", "id":"a7e03f23-a206-4668-aff6-bcaae81f5a86", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"e71bd6ad-e57d-4804-9cbe-158c5cf65855", "starting_time": 90, "ending_time": 95, delay: 5, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"527712b1-0c80-4fe0-91cb-50dce6cce814", "starting_time": 95, "ending_time": 100, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"68ed4497-43b3-4ffa-96e8-e58a6c492ff5", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task B (Sim40)", task: "User Task B", "id":"b9a1c200-6e3c-420f-8c06-c5fca5d65108", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"e336e63e-b9b0-4cfb-943d-0eec877f113b", "starting_time": 810, "ending_time": 815, delay: 5, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"89488d3c-de5a-4ee2-a144-db950cdd1705", "starting_time": 815, "ending_time": 820, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"8975d455-6472-427b-b797-171da6fa8ca1", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"eda11d6f-d24a-47f2-aa7e-2b8a5316c65c", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"1f3d10d0-b9cc-46bd-994f-591672ad4a37", "starting_time": 830, "ending_time": 835, delay: 5, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"3ab8c36c-3b1f-4d76-94c1-46d023c662c0", "starting_time": 835, "ending_time": 840, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"26094b52-f3b4-44df-b206-36c9cd44a3a1", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"17bd7bfa-d3f4-449d-bd88-cc92e6a1f92d", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"1fae9fc6-64c0-48ec-9abb-284fa064d3c6", "starting_time": 850, "ending_time": 855, delay: 5, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"0223b1fc-8043-4dfa-a1df-87729bfd3935", "starting_time": 855, "ending_time": 860, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"45c2a143-772e-496f-ad61-aa2293d99a8e", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"318a731a-ff59-4d8e-a581-7c12e06d2c4b", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"716cc72f-2c70-4179-aa01-c75a00a48d37", "starting_time": 870, "ending_time": 875, delay: 5, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"fef7ff7e-c0a5-4a29-b668-f6ec5b373352", "starting_time": 875, "ending_time": 880, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"0d71a254-15d1-4455-85df-aa8a1a07567c", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"36c18aa2-4466-4afd-b65f-c099f3dde9f1", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"7353c577-6169-4ca2-87c5-7f9c81f28279", "starting_time": 890, "ending_time": 895, delay: 5, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"d77f7053-0fae-48f8-afa4-ac95ef177531", "starting_time": 895, "ending_time": 900, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"dc2367b3-1ea2-4282-827a-c059fc542a38", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"82ba66ca-7438-4cdd-ba33-98409f47ed54", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"dc3e6b42-7afd-4704-8393-51b6c7f2c258", "starting_time": 910, "ending_time": 915, delay: 5, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"cb3d2567-3f81-48d5-9222-80c6d6a63041", "starting_time": 915, "ending_time": 920, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"2e813bea-c5c0-446b-8e84-59270e279f04", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"c8842b69-0104-4006-ab6d-1113c7d28eef", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"1bbd08c6-05db-4cd9-b69f-b058d7d35cdf", "starting_time": 930, "ending_time": 935, delay: 5, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"363bdb83-df37-4e8b-a825-82799d74ba5b", "starting_time": 935, "ending_time": 940, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"51c7d2db-a9dc-4766-b125-f4058af5e135", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task B (Sim47)", task: "User Task B", "id":"e7ff9549-ac16-48ef-9539-29479d729aac", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"5a9b3933-9413-4fb5-b04b-35274b983923", "starting_time": 950, "ending_time": 955, delay: 5, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"9475721d-6dec-4a07-84ea-ed29a840ec9e", "starting_time": 955, "ending_time": 960, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"1a5f3c8c-1557-48b0-800d-a0e2cc86676a", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"f253139a-6490-4fc5-aa9f-15d977633ba4", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task B (Sim48)", task: "User Task B", "id":"31d26f9a-fed9-4ccf-849e-157785cd4415", "starting_time": 970, "ending_time": 975, delay: 5, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"2edbbf8a-c050-4f0e-bdb8-b7642fc84e6c", "starting_time": 975, "ending_time": 980, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"54a82e4f-58c1-49bc-9d68-f0881960ed16", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"b01e85a3-4e87-4410-a2ac-8edb266cd7a4", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"362dd81d-7f93-47bc-abd7-39c7dab61f65", "starting_time": 990, "ending_time": 995, delay: 5, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"aac7f847-3c21-431b-ae28-93ca9428f80a", "starting_time": 995, "ending_time": 1000, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"59f8274c-acb9-4795-a5f8-6a2fc2915ec7", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"b1021573-d7e1-48c7-b851-ca60d77285a3", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task A (Sim5)", task: "User Task A", "id":"1e25cc0d-3ab9-498f-b810-7ae0fdc440c9", "starting_time": 110, "ending_time": 115, delay: 5, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"153fb1e9-2111-4cb8-a0a4-68817ca5bf64", "starting_time": 115, "ending_time": 120, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"4632b7eb-fec6-4590-a547-8b13b2f2eab0", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"99be87f5-f2fb-4a23-b286-f667bd1045a4", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"9ab347b4-5d61-48a6-a710-8710a71e6233", "starting_time": 1010, "ending_time": 1015, delay: 5, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"abcf5c61-3f6e-4c99-a3c4-9dd8a6d9c1e2", "starting_time": 1015, "ending_time": 1020, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"5fa9c80d-7a62-4c6a-9d6b-ee8be4b9a4f2", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"039c24a2-5bcd-4485-9b44-bf9206855361", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"0ca20bbd-f974-43ae-8e4d-601a9a036e2a", "starting_time": 1030, "ending_time": 1035, delay: 5, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"d78c6568-b5a5-4af4-ad0a-83de7049d9db", "starting_time": 1035, "ending_time": 1040, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"0262460b-726a-4754-bcde-ac4f34fc99c1", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"da9041ea-3505-4766-9e7d-e5bd73aa30ff", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"4c3ba4f6-42ef-4c92-ae0e-2dfa1ec80f83", "starting_time": 1050, "ending_time": 1055, delay: 5, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"422e91f2-4864-4daa-92e0-d1c56b076021", "starting_time": 1055, "ending_time": 1060, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"f8754956-71a8-4c61-b6f1-144e8fee005d", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"bfcbd4e6-e655-4e64-a8eb-c5b0d1c1bfb1", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task A (Sim53)", task: "User Task A", "id":"79e35b8d-2c07-417f-bc58-cb402078c204", "starting_time": 1070, "ending_time": 1075, delay: 5, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"d298016a-47b5-4e65-9443-975279c52935", "starting_time": 1075, "ending_time": 1080, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"30c1b8c4-2bfa-4a02-b2dc-cfa4ad755b17", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task A (Sim54)", task: "User Task A", "id":"17ff6e92-86dc-4aa4-9a22-e8fe62a5f845", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"370b153c-677d-4cfd-912e-1521ff41bab0", "starting_time": 1090, "ending_time": 1095, delay: 5, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"2cbbbb16-4923-4687-9cbb-3562d0b86498", "starting_time": 1095, "ending_time": 1100, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"ab6a5507-4f19-4cdf-b2c2-1004f42a5a56", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"8de16d69-a6b8-4d97-80ab-b7ee2fc6a1ae", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"f3fb1e3a-146a-484c-b879-327874ae1853", "starting_time": 1110, "ending_time": 1115, delay: 5, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"b6353724-c0d0-42ac-9f35-64b45ab27156", "starting_time": 1115, "ending_time": 1120, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"3b226f1c-fb22-40b2-9dc5-3c6014ce9d86", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task B (Sim56)", task: "User Task B", "id":"ee89c5a3-8c56-4add-8bdb-e80432f89453", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"07f43a0a-fdf6-426b-adf8-8c56ae5bb30f", "starting_time": 1130, "ending_time": 1135, delay: 5, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"1c28f9c8-3aeb-463e-b1c7-125e36f5b598", "starting_time": 1135, "ending_time": 1140, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"f75178af-8e5a-4a69-b6ad-b16186b2bdf0", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"e2e7d487-eaf7-47f0-afd7-cc948f816a8a", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"17cd5ea2-41ae-41da-9ad2-c04d58b0a6cb", "starting_time": 1150, "ending_time": 1155, delay: 5, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"d3be5dd8-d8b2-489b-86bd-d654e37a2240", "starting_time": 1155, "ending_time": 1160, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"9d7f3d81-b250-48a6-84a4-4f7bf117529d", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"08e275c7-aa6a-4636-be54-72e6aced4715", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"7072e544-3e49-43db-9817-fd55610875a2", "starting_time": 1170, "ending_time": 1175, delay: 5, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"1820acd7-c78a-48a0-93e3-056e87d9cf4e", "starting_time": 1175, "ending_time": 1180, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"110dc9e1-4158-4863-a6d0-114fa0b23d1c", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task B (Sim59)", task: "User Task B", "id":"b770116f-0bc7-4d23-ac3f-cecca77bd451", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"f9e0ea9f-4e22-4695-bfdd-541ad9e2068b", "starting_time": 1190, "ending_time": 1195, delay: 5, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"d2249e13-cc90-49f0-b82f-ec6c061568cd", "starting_time": 1195, "ending_time": 1200, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"e8ddd716-6acc-40a4-8bb2-561560214cec", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"bb287dae-6bea-4205-ba04-07dc11a6a338", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"cc8c0c6a-d98d-4524-b7bb-5feebc9a7bef", "starting_time": 130, "ending_time": 135, delay: 5, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"5e589e13-366e-4cc6-a480-f5e596186af3", "starting_time": 135, "ending_time": 140, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"05bc6f0f-07cd-4f41-8bf5-f9d0b431f760", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task A (Sim60)", task: "User Task A", "id":"8c683021-d8d2-448f-b066-4a4987901752", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"d232076e-220b-42c7-bc78-78f35e824535", "starting_time": 1210, "ending_time": 1215, delay: 5, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"df3c98ce-131f-41c9-8ed2-a4950432be62", "starting_time": 1215, "ending_time": 1220, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"035b78c1-5b0f-4fd0-91a5-99d7073160d9", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"2745c87c-fe51-4e34-9890-2211d27ff038", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task A (Sim61)", task: "User Task A", "id":"7492654b-6ac0-4b30-8403-65f446b24141", "starting_time": 1230, "ending_time": 1235, delay: 5, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"d9e23cba-5960-41a3-b8b2-ea76d030f6f3", "starting_time": 1235, "ending_time": 1240, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"87eeb5af-4b91-4ff3-bfb5-d8e4960f7528", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task A (Sim62)", task: "User Task A", "id":"96c509f4-7fd8-46e3-b714-5186cc55544f", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"6ebfe2d5-c291-4a6d-a86c-eff1bc208565", "starting_time": 1250, "ending_time": 1255, delay: 5, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"48e95f95-ce2f-4c54-bb93-f01161dd5539", "starting_time": 1255, "ending_time": 1260, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"a500ab0e-873e-46da-b11c-4ddc5044c28a", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"168fe85e-30e5-474e-8be5-cf5c5fae5c32", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"739bcb4c-2365-47ee-aa8f-e4beee5b791b", "starting_time": 1270, "ending_time": 1275, delay: 5, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"43e0b8d3-7bda-4204-ac41-c026a38f03ce", "starting_time": 1275, "ending_time": 1280, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"f0c1cba5-e2b7-40b8-88de-c4b1ac7fda26", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"267eca7e-acb8-41d2-94b6-20c1bfe62666", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"49d49cd4-7da8-4fc0-bea5-29f9d890e1e8", "starting_time": 1290, "ending_time": 1295, delay: 5, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"1d077d1e-f17f-40f6-83a9-325b892cd709", "starting_time": 1295, "ending_time": 1300, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"d0191f50-c5b8-4210-bce0-6461a8c9b933", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"0e58437d-5caa-4a6e-84df-d2ec0d6f2b47", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"1a63e63a-2999-4ae9-991f-600ed199b4c6", "starting_time": 1310, "ending_time": 1315, delay: 5, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"b05b63d5-6de6-4d40-9515-f0f0b2238c8b", "starting_time": 1315, "ending_time": 1320, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"f88cd2a3-322a-49a6-af92-bc7875681bce", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"81436282-b4c5-4242-aa71-36a2fc831de3", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"5ac24630-e53a-4d42-99d6-fdcc5c70b326", "starting_time": 1330, "ending_time": 1335, delay: 5, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"6ef06885-cac5-439f-8740-7284d6f761cf", "starting_time": 1335, "ending_time": 1340, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"84a9b50a-e4a2-4b33-a477-2ad4dee66097", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"dff31545-72ef-4964-832f-9c7ee5c591eb", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"e6a4314f-4c49-495f-853b-715735fa9de6", "starting_time": 1350, "ending_time": 1355, delay: 5, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"9deb5706-025c-4f44-8d0e-7c9d3d6b443a", "starting_time": 1355, "ending_time": 1360, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"40648cbf-7802-4e5e-935a-0983ab02df57", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"cc7d27f4-9aeb-4be9-be0a-5803552aca22", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"21cc33a6-615d-4ad2-b312-d80ef3fb3d4a", "starting_time": 1370, "ending_time": 1375, delay: 5, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"9d43bc6a-4435-4332-a590-0c161b939c84", "starting_time": 1375, "ending_time": 1380, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"b26e293c-187c-45ca-bf26-302a402cb46b", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"34471685-546b-4ba4-b1c1-26f248f831d9", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task B (Sim69)", task: "User Task B", "id":"6194f423-bfdb-489c-980e-3fc7599f18db", "starting_time": 1390, "ending_time": 1395, delay: 5, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"a24877cc-827a-458e-bd57-f4dbe79d8660", "starting_time": 1395, "ending_time": 1400, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"25d9ccb7-e432-484f-8eec-d6952757887e", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task B (Sim7)", task: "User Task B", "id":"e571b266-306a-47f4-8c44-3bfcd10a657e", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"7ac27f2f-5f57-4ea8-858e-0d9baa095d17", "starting_time": 150, "ending_time": 155, delay: 5, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"4efd447c-350e-45d8-a8ae-92d559cd2097", "starting_time": 155, "ending_time": 160, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"ab7c28d2-f54a-42bc-851d-95409fbf3ad4", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"71e5f0c9-4255-4b1f-89c1-792a3677fefd", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task A (Sim70)", task: "User Task A", "id":"7cfa78dc-84cf-488f-8ed7-309750a0c4f9", "starting_time": 1410, "ending_time": 1415, delay: 5, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"5a44bfe2-4f6a-411a-bbe1-7ef8e234ddb4", "starting_time": 1415, "ending_time": 1420, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"c0c6e8fe-a005-47a3-83f2-40c6475af8ea", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"bc1bd018-0bda-42d4-994e-deefb9f03d22", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"35d49f38-133e-4c07-87f6-78321ebf5757", "starting_time": 1430, "ending_time": 1435, delay: 5, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"a824b120-b90a-4dc2-a8ea-cb9e99d12b9d", "starting_time": 1435, "ending_time": 1440, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"6f9e435d-9141-4d92-b7ac-d288e2de4154", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"d077309e-4a93-4128-a2fa-05c1a3cbe019", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"27d3d4c0-56bd-4d6c-a2db-7109ebf83db6", "starting_time": 1450, "ending_time": 1455, delay: 5, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"ccca46c6-52da-4692-89b1-d3e6e5b968b8", "starting_time": 1455, "ending_time": 1460, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"be1058a8-447e-4947-9429-f8dab529143c", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task B (Sim73)", task: "User Task B", "id":"85368850-dd16-41d9-a96f-eb79ff3ba382", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"3dfd33e4-c0fa-4b84-bb10-d9c1fd36f88c", "starting_time": 1470, "ending_time": 1475, delay: 5, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"b42f08a4-d7da-4982-9c06-576e2307b37e", "starting_time": 1475, "ending_time": 1480, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"100f6734-c3f6-43f6-ba9d-029f4a352659", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task A (Sim74)", task: "User Task A", "id":"d77951c5-3588-4e69-8f14-42d117fd9e48", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"0e2e993f-b680-4053-b6fb-79fe61c47e48", "starting_time": 1490, "ending_time": 1495, delay: 5, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"b26892cb-0a9e-4e9d-99d4-a21e95fc327e", "starting_time": 1495, "ending_time": 1500, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"c7e30991-2dd4-4487-b504-2660fd20c88f", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"f1bc2bec-ed95-48d8-8867-aa60f0003649", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"f6f8bf06-9a30-4d34-b48a-4ce39d71698a", "starting_time": 1510, "ending_time": 1515, delay: 5, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"6136da08-f9fd-4bbd-b7a2-0702566077e8", "starting_time": 1515, "ending_time": 1520, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"16f37496-2758-48a9-a12b-6507511facbb", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"e590fa43-4c3d-4d2f-8a7e-d9e8a70c04be", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task B (Sim76)", task: "User Task B", "id":"f73866e0-1b3a-4e0a-ae51-58f0e6007443", "starting_time": 1530, "ending_time": 1535, delay: 5, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"2b1dee77-f9bd-4e30-acd0-aa9c5e9ed66f", "starting_time": 1535, "ending_time": 1540, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"97bd8f43-daaa-4834-8183-97827157ec0b", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task B (Sim77)", task: "User Task B", "id":"d86f998e-cd80-4f54-b1ee-34881d6f6704", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"47ea21ed-a890-48a9-90eb-127a8fef61a4", "starting_time": 1550, "ending_time": 1555, delay: 5, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"ca3db503-bfb7-4790-a950-a74dcbe94bab", "starting_time": 1555, "ending_time": 1560, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"3027667c-2647-4d69-b593-3ca3b0abc7b4", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"b0e7ece7-c71c-420a-b4fc-18bca646ef86", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"117e65d8-7999-4298-aafb-e788bffa19b3", "starting_time": 1570, "ending_time": 1575, delay: 5, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"ae7b015d-e8b2-4006-872b-805f0d6a97d8", "starting_time": 1575, "ending_time": 1580, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"fdc7ded1-98e5-4bb4-9d26-2316316340ca", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"90209800-27e1-4096-854f-d447c152ce13", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"7b612738-81f4-4c92-9f4c-dfa6d28b6e10", "starting_time": 1590, "ending_time": 1595, delay: 5, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"45705ebd-e466-4961-a845-47a1010da443", "starting_time": 1595, "ending_time": 1600, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"006ec548-5396-40e1-8faf-a6849a38e549", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"f6a59ea8-82cd-44a4-8a6d-d6e15169b90e", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task A (Sim8)", task: "User Task A", "id":"66211184-55da-4abe-95e3-121024d12ee8", "starting_time": 170, "ending_time": 175, delay: 5, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"732ce91a-daa1-4242-81ae-6f5f3033d68a", "starting_time": 175, "ending_time": 180, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"e55ff4e1-368b-4d0d-8974-e1ccf67892cc", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"edb26512-51b3-4fed-a40e-271ac1d10805", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"f1c37090-e412-4649-b06e-977cb28ebc54", "starting_time": 1610, "ending_time": 1615, delay: 5, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"dcb4ac10-24df-435f-9767-dac89f146b64", "starting_time": 1615, "ending_time": 1620, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"58272965-7416-44dc-a1ac-dace00c4d1e1", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task A (Sim81)", task: "User Task A", "id":"863f9650-275b-4592-a5dc-7c48e16f9933", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"7972f9c8-9a11-4ed0-a6b2-efc1c0528314", "starting_time": 1630, "ending_time": 1635, delay: 5, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"af3d018f-f8b2-46c9-850e-6a39a20b8e50", "starting_time": 1635, "ending_time": 1640, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"7afb98c3-2984-4b10-886f-3d69ae7793d6", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task B (Sim82)", task: "User Task B", "id":"e34f7f40-a441-46da-948f-2e1503500d98", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"3ba3460c-abf7-484c-a2d0-5e93c140c2a8", "starting_time": 1650, "ending_time": 1655, delay: 5, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"d9e46271-acd1-49b8-97ec-39129ea070c0", "starting_time": 1655, "ending_time": 1660, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"b4d703fb-7b5f-4ea1-ae6a-27527e4e01e7", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"1e4f71dc-6dd8-4404-96fd-1fc08bfdbe82", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task B (Sim83)", task: "User Task B", "id":"4d957c6d-2834-41c0-b17d-6932ade8e27b", "starting_time": 1670, "ending_time": 1675, delay: 5, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"e7f6f4d3-5cc2-4734-a07e-113df45f338c", "starting_time": 1675, "ending_time": 1680, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"e2e6b90d-3677-4a23-b92c-2083260abbd6", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"ab93a3bf-5e4e-493a-8d70-c00bb4002128", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"f0b85dcb-ca5e-47ae-bb2b-f7ad2ec63212", "starting_time": 1690, "ending_time": 1695, delay: 5, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"544c23fe-89ea-45f2-bea6-09f3fac02b7f", "starting_time": 1695, "ending_time": 1700, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"fae73964-9ae3-4f2a-a5a8-7567c9ccda9e", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task A (Sim85)", task: "User Task A", "id":"e5ba2f69-5962-4a4f-8ac4-93f87a7d347c", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"0606db8d-fa01-4b9a-8586-7888bb87bad7", "starting_time": 1710, "ending_time": 1715, delay: 5, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"8e8be4b8-5a1d-4a87-bf8c-8e06ee527dd8", "starting_time": 1715, "ending_time": 1720, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"bce451e0-01a7-4887-a9cc-766028eef58a", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"c879a23d-7b6d-4362-beb8-a6cc3900073a", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"2363dc45-118e-4a91-81ee-7e786a596e11", "starting_time": 1730, "ending_time": 1735, delay: 5, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"948ebc1a-9d29-48b7-bd31-f39b6aef0068", "starting_time": 1735, "ending_time": 1740, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"bce9fd16-998c-4371-971f-d5f13b37fb83", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task B (Sim87)", task: "User Task B", "id":"06783bfb-55bb-434d-950f-f5726313ddae", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"5485c761-64f4-49e8-a393-1ff41c42bbdb", "starting_time": 1750, "ending_time": 1755, delay: 5, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"f7626cbc-e04c-4d2c-a256-068919b548af", "starting_time": 1755, "ending_time": 1760, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"c7c88a51-621c-4093-93fd-5a1714711946", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"62c7eed9-780a-409e-af0f-1f9aabc7355f", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"71c2176e-53cb-42ea-89ea-40fb0d4202a6", "starting_time": 1770, "ending_time": 1775, delay: 5, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"fc0c1d0b-86e9-43ea-968f-096330725ea7", "starting_time": 1775, "ending_time": 1780, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"bc595603-8091-4583-a150-cd4bd4af7ab1", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"e1d3e886-2dfc-4f8b-9578-00561b2d0af9", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task A (Sim89)", task: "User Task A", "id":"01b4bbee-8ad2-4356-b488-844cfdbc849d", "starting_time": 1790, "ending_time": 1795, delay: 5, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"c3436dfb-af86-44c9-827f-357dafc2c0ad", "starting_time": 1795, "ending_time": 1800, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"49697610-876b-4c11-9d1f-352c7e4e7e53", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task A (Sim9)", task: "User Task A", "id":"e0402ad1-d5c6-4b46-a5db-9fd4db5bc1a8", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"297ab85a-2f93-4427-96b6-b7562f02aacd", "starting_time": 190, "ending_time": 195, delay: 5, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"f84fc2d0-a8cf-4b3a-8f64-2dcdeecd46e5", "starting_time": 195, "ending_time": 200, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"ed0398d2-12eb-4abe-ab6b-1843d53dfb91", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task A (Sim90)", task: "User Task A", "id":"860bf94a-aa4f-4603-ade2-3f9bd552f53d", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"9d06f2bb-62a4-4242-8637-7421f03d3d41", "starting_time": 1810, "ending_time": 1815, delay: 5, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"af7bee59-da18-41b8-8987-b7ac30780f97", "starting_time": 1815, "ending_time": 1820, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"bf70bc88-76de-4050-b9ad-9905e4af3af2", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"b7c1a97d-1cc5-4be6-9fee-50df165d2e02", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"6a739c67-7ef3-4324-b0e0-5a4178d9cee2", "starting_time": 1830, "ending_time": 1835, delay: 5, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"f8926f40-8d79-41a4-a8f8-f136d74840f4", "starting_time": 1835, "ending_time": 1840, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"1e3ec098-d374-42ca-a6b9-dc5ea256819a", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"926b8d2c-e26d-4ae3-89b6-4bba5b004a99", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"ac8533f0-3630-4739-a6cd-6553d05089eb", "starting_time": 1850, "ending_time": 1855, delay: 5, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"f9f7d413-5f6e-4670-83e9-13346a9229f3", "starting_time": 1855, "ending_time": 1860, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"bf281ff7-994e-4b45-96b7-48328f6736e5", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"8de635bf-403f-4267-a184-bcd117b1c17d", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"602329ae-3d95-423d-97cc-47654e092498", "starting_time": 1870, "ending_time": 1875, delay: 5, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"9a8e6e47-03e7-4e6a-b963-991a4a5c8f4d", "starting_time": 1875, "ending_time": 1880, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"d9bb48ca-a148-49f6-a63a-dca5d85a87cb", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task A (Sim94)", task: "User Task A", "id":"d7d72cb2-aa97-4116-be1a-bde0b7a21a0c", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"e83e3d8c-d5bc-4ea2-a6ef-5e76354e3a13", "starting_time": 1890, "ending_time": 1895, delay: 5, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"9d2f0fce-b86d-4a3f-9dad-e82d02f3c1cf", "starting_time": 1895, "ending_time": 1900, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"a3e138df-a3e3-4c9a-b21c-c5675414bd6c", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"9ff6871e-3f9c-430f-8d50-b1b0c6b21f25", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task B (Sim95)", task: "User Task B", "id":"b1c16e50-8357-48fc-87a3-564892dddaa9", "starting_time": 1910, "ending_time": 1915, delay: 5, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"9abe1f4d-8c80-4060-a5cb-244c7120469e", "starting_time": 1915, "ending_time": 1920, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"9bb5de93-dda4-42c2-b43d-9c434becae89", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"8b5f930e-7be3-4b03-bb5a-21018941f2ed", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"ed50f95d-7839-4e39-8836-bf28c413459d", "starting_time": 1930, "ending_time": 1935, delay: 5, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"9a402115-d28c-4f5b-b7f0-78847d3a8ae5", "starting_time": 1935, "ending_time": 1940, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"88b238e2-7257-4ce1-ba66-16d5f41c0c26", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"3c9686c6-47ac-4954-bc13-3e838382249e", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task A (Sim97)", task: "User Task A", "id":"56584aa5-6396-498a-899f-08e080440c23", "starting_time": 1950, "ending_time": 1955, delay: 5, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"e346fb3b-58b6-42ce-b7ee-ae2548c22558", "starting_time": 1955, "ending_time": 1960, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"6321ae93-b1aa-4b51-a9b2-77a6c1c7d8c5", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"934d3303-a2db-45d5-969c-87d20ae318ba", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"d198fe3e-62cf-45a4-9e06-a893f2a2d516", "starting_time": 1970, "ending_time": 1975, delay: 5, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"5dbf66d1-7896-4b8c-bbf5-1807b22d85ca", "starting_time": 1975, "ending_time": 1980, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"5546a3e0-7fdd-4185-882a-85dd6fe434fc", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"deae4186-06e0-4028-bf24-527bcdc1640f", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"User Task A (Sim99)", task: "User Task A", "id":"eb02fe45-7717-496c-9b88-d17763565010", "starting_time": 1990, "ending_time": 1995, delay: 5, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"d0f496d7-991f-46e9-a943-14377be1f360", "starting_time": 1995, "ending_time": 2000, delay: 0, cost: 0}
]},
];
