var tasks = [
	"Approve customer",
	"Over 21",
	"Under 21",
	"Validate customer",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Approve customer (Sim0)", task: "Approve customer", "id":"dd2184e2-22da-47ef-b40e-dcdc1a5ab605", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Under 21 (Sim0)", task: "Under 21", "id":"b4e58c24-244c-4431-8d06-2d85ff851df4", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0},
	{"label":"Approve customer (Sim1)", task: "Approve customer", "id":"6cf52202-852b-4cbb-ae00-afd3570b3729", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Over 21 (Sim1)", task: "Over 21", "id":"3d5c2871-6c98-4b6b-8d7b-aa6e42b19326", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0},
	{"label":"Approve customer (Sim2)", task: "Approve customer", "id":"2523f4fd-7d8b-4a4f-b647-0f319c84e9ae", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Under 21 (Sim2)", task: "Under 21", "id":"7bbf17e8-2ecd-43dd-bc13-efaf3cf6fa58", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0},
	{"label":"Approve customer (Sim3)", task: "Approve customer", "id":"bb47f39d-65ad-4096-b208-8a28229356b9", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Over 21 (Sim3)", task: "Over 21", "id":"7b733358-62bc-41db-a443-f5e0694adaa2", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0},
	{"label":"Approve customer (Sim4)", task: "Approve customer", "id":"8c38c789-d18b-4d9a-8746-dbadd6cbb995", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Under 21 (Sim4)", task: "Under 21", "id":"b1ce505b-c17f-47c9-8d2c-3ad980e153b1", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0},
	{"label":"Approve customer (Sim5)", task: "Approve customer", "id":"d18f5132-8065-4297-805d-6b1c9dd43025", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Over 21 (Sim5)", task: "Over 21", "id":"334c5bd0-277b-42bf-86c8-eecbeb40c9fd", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0},
	{"label":"Approve customer (Sim6)", task: "Approve customer", "id":"e5cfb5da-7f4d-4c57-b6f1-f2c5a63fa3db", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Over 21 (Sim6)", task: "Over 21", "id":"b8f2946a-ff50-4869-9030-5dcaf0f9c9a9", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0},
	{"label":"Approve customer (Sim7)", task: "Approve customer", "id":"d32b031c-c695-43c9-b56a-986047c67b89", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Under 21 (Sim7)", task: "Under 21", "id":"0afc692a-73e2-4559-8174-1d7ada7c7e18", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0},
	{"label":"Approve customer (Sim8)", task: "Approve customer", "id":"5a3706f3-0136-4938-843f-035e8fb232e2", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Under 21 (Sim8)", task: "Under 21", "id":"0987d54b-780d-4e96-90f5-591af38cc7cc", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0},
	{"label":"Approve customer (Sim9)", task: "Approve customer", "id":"cf03b04c-4652-45e4-989f-b8557710404f", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Under 21 (Sim9)", task: "Under 21", "id":"9184df7d-714c-489c-9b0b-ed81e70d7a95", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0},
	{"label":"Approve customer (Sim10)", task: "Approve customer", "id":"786b0ef6-3ba9-4cbf-84dc-373f5e44fafe", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Over 21 (Sim10)", task: "Over 21", "id":"93118742-d8c7-4818-92ab-653a514d3db3", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0},
	{"label":"Approve customer (Sim11)", task: "Approve customer", "id":"e51ea4e9-e45e-442f-a8a6-0f3ac1855303", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Under 21 (Sim11)", task: "Under 21", "id":"344d63b6-7139-49ad-a7b5-ccf9ddc5dc11", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0},
	{"label":"Approve customer (Sim12)", task: "Approve customer", "id":"17800395-c6a7-4ad5-bcba-b13a064f063f", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Under 21 (Sim12)", task: "Under 21", "id":"7ec11883-7e2a-4210-980b-6a50787a6923", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0},
	{"label":"Approve customer (Sim13)", task: "Approve customer", "id":"fbfd6501-c509-40d2-9dad-aca8cef159c8", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Over 21 (Sim13)", task: "Over 21", "id":"c3bc2565-f8a4-4208-afba-8778761ae794", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0},
	{"label":"Approve customer (Sim14)", task: "Approve customer", "id":"1ddbd8c2-de7b-40b1-b3a9-561d3fc1940a", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Under 21 (Sim14)", task: "Under 21", "id":"81463d8d-3d23-4f2d-8076-9d38ab698a16", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0},
	{"label":"Approve customer (Sim15)", task: "Approve customer", "id":"f693c29d-0b51-470b-97a5-ab31f6b33a31", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Under 21 (Sim15)", task: "Under 21", "id":"7a5c7413-263a-4df6-bad3-afc1ab170ab9", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0},
	{"label":"Approve customer (Sim16)", task: "Approve customer", "id":"fc14985d-e52c-4e0d-a0a6-6eeab5b741f7", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Under 21 (Sim16)", task: "Under 21", "id":"81ad03aa-f184-4af0-9444-ee473c6a13a9", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0},
	{"label":"Approve customer (Sim17)", task: "Approve customer", "id":"f578d283-7fa6-4f4f-a9cf-0eb88a718b74", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Over 21 (Sim17)", task: "Over 21", "id":"327ddd0b-6df7-4046-badd-0903b639f583", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0},
	{"label":"Approve customer (Sim18)", task: "Approve customer", "id":"db63d8a1-ab97-4890-b3ce-cefb4297add7", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Under 21 (Sim18)", task: "Under 21", "id":"41c5de38-0206-4dda-9822-398183b52ac0", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0},
	{"label":"Approve customer (Sim19)", task: "Approve customer", "id":"2454788f-71c3-4581-b7ee-a65b8433af20", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Under 21 (Sim19)", task: "Under 21", "id":"b426dd94-ec7c-4858-88d0-62a094abf860", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0},
	{"label":"Approve customer (Sim20)", task: "Approve customer", "id":"6656d034-c06b-41d2-8b6b-1e3bbf11050d", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Under 21 (Sim20)", task: "Under 21", "id":"2c0c68b2-8310-4c96-9b6e-5b620f674645", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0},
	{"label":"Approve customer (Sim21)", task: "Approve customer", "id":"74f9d38f-ff64-439e-83cc-e8a367d40043", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Under 21 (Sim21)", task: "Under 21", "id":"d55622fa-56d2-49e3-99ab-e75e336c25a6", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0},
	{"label":"Approve customer (Sim22)", task: "Approve customer", "id":"573d4756-155d-4962-b453-ce5dd5d1fedd", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Under 21 (Sim22)", task: "Under 21", "id":"13b4efed-ca4f-469b-b6b9-6ac7e7a25947", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0},
	{"label":"Approve customer (Sim23)", task: "Approve customer", "id":"15c6b84b-0cec-4a39-825c-76b9fb127f44", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Over 21 (Sim23)", task: "Over 21", "id":"cce9f2b2-80c3-4c20-a983-0d71e5310a0c", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0},
	{"label":"Approve customer (Sim24)", task: "Approve customer", "id":"c751f70e-20b5-45be-ad15-6fd12eb89a65", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Over 21 (Sim24)", task: "Over 21", "id":"66e06d02-a221-43f7-9cdf-5cc29b728cc1", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0},
	{"label":"Approve customer (Sim25)", task: "Approve customer", "id":"09460a5a-7804-40ac-bdd7-29aa14f1420c", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Over 21 (Sim25)", task: "Over 21", "id":"731c49b2-e6a3-4712-ba0b-64f0f0970880", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0},
	{"label":"Approve customer (Sim26)", task: "Approve customer", "id":"35f62a3d-f17c-4809-8305-1d8f9498c06e", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Over 21 (Sim26)", task: "Over 21", "id":"930279d2-750f-4935-8580-486662772b73", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0},
	{"label":"Approve customer (Sim27)", task: "Approve customer", "id":"7c98c1d3-f0db-40d0-98ce-ab20ddc16434", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Under 21 (Sim27)", task: "Under 21", "id":"51bcf3a7-4dc8-4b0d-aa8b-431092952cdb", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0},
	{"label":"Approve customer (Sim28)", task: "Approve customer", "id":"f2c94a55-3470-4282-baa5-1aa25c0f34f5", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Under 21 (Sim28)", task: "Under 21", "id":"7fc599ac-147e-47b3-9a26-ca43de4d3a7c", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0},
	{"label":"Approve customer (Sim29)", task: "Approve customer", "id":"37f7ebc1-9730-4eb0-9ca6-530934e39174", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Over 21 (Sim29)", task: "Over 21", "id":"ee6e632c-9020-4737-9b49-6d34d668597c", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0},
	{"label":"Approve customer (Sim30)", task: "Approve customer", "id":"686dc138-d72d-4d6a-a889-ba4964810f65", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Over 21 (Sim30)", task: "Over 21", "id":"9095fd84-2fd5-4f15-aa2a-2e1b62ca18f4", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0},
	{"label":"Approve customer (Sim31)", task: "Approve customer", "id":"146bf8f0-e5f2-491b-a68b-dfc5d4d6727d", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Under 21 (Sim31)", task: "Under 21", "id":"83300975-928a-4a83-980c-19b0c1c30073", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0},
	{"label":"Approve customer (Sim32)", task: "Approve customer", "id":"0d4535bb-b623-4bfc-a0f5-37cd1a62f0f0", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Over 21 (Sim32)", task: "Over 21", "id":"c4bd4cf0-05de-429e-8728-b329bc49d306", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0},
	{"label":"Approve customer (Sim33)", task: "Approve customer", "id":"e9f92338-9f8e-4287-9f16-a5aac1d718ec", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Under 21 (Sim33)", task: "Under 21", "id":"954a9106-4bf1-4ed9-9f16-ba65545f55ec", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0},
	{"label":"Approve customer (Sim34)", task: "Approve customer", "id":"9c026c41-918c-496a-b543-11d59256fea9", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Under 21 (Sim34)", task: "Under 21", "id":"25afae49-950b-4004-9101-44ea0cd7f428", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0},
	{"label":"Approve customer (Sim35)", task: "Approve customer", "id":"ee0b0df7-fdeb-433b-b848-3bc2d40d3ae0", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Under 21 (Sim35)", task: "Under 21", "id":"633fa1de-aab0-45cb-9430-371650a4faae", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0},
	{"label":"Approve customer (Sim36)", task: "Approve customer", "id":"95f2d23b-efd6-4eb0-ba4f-05c3c0429ea6", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Over 21 (Sim36)", task: "Over 21", "id":"f3c3d55d-45ff-46cc-a292-d50893248844", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0},
	{"label":"Approve customer (Sim37)", task: "Approve customer", "id":"0a61d62e-2af4-43f5-822a-8eefaa18d54e", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Under 21 (Sim37)", task: "Under 21", "id":"7c063329-0888-4632-a68d-30744864e205", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0},
	{"label":"Approve customer (Sim38)", task: "Approve customer", "id":"ac92e8fd-5bca-42c4-bc78-a3a8ab6f6722", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Under 21 (Sim38)", task: "Under 21", "id":"69b1696d-803c-4eae-b311-3eeba5883038", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0},
	{"label":"Approve customer (Sim39)", task: "Approve customer", "id":"d7f35d03-9183-440d-9fc5-66e6370c67cc", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Under 21 (Sim39)", task: "Under 21", "id":"4750649b-3faf-4587-97fe-a3ccaf291668", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0},
	{"label":"Approve customer (Sim40)", task: "Approve customer", "id":"2f6b71c8-cc96-4a06-bff4-86f6725ebce5", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Over 21 (Sim40)", task: "Over 21", "id":"5be9dc82-7df1-4efc-8de2-24022a443351", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0},
	{"label":"Approve customer (Sim41)", task: "Approve customer", "id":"34ed80e6-17e8-416e-8eac-00ac051c1fd5", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Under 21 (Sim41)", task: "Under 21", "id":"36171d25-13ab-4e73-b9b3-7ffe50afc863", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0},
	{"label":"Approve customer (Sim42)", task: "Approve customer", "id":"69fcb050-951f-44e8-a6c9-74d3bb2f8271", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Over 21 (Sim42)", task: "Over 21", "id":"cee8affd-8bb9-40f3-a024-12b6d3318084", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0},
	{"label":"Approve customer (Sim43)", task: "Approve customer", "id":"fcd990ba-b194-4f20-8ebb-c76130157472", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Under 21 (Sim43)", task: "Under 21", "id":"ff356134-d3f2-43ae-87ae-162c7775370d", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0},
	{"label":"Approve customer (Sim44)", task: "Approve customer", "id":"40d2a6dd-a1b7-496f-816d-3674ecb18891", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Over 21 (Sim44)", task: "Over 21", "id":"59085409-8143-4680-a85f-2ab6e12f48ae", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0},
	{"label":"Approve customer (Sim45)", task: "Approve customer", "id":"f50021af-405b-4e42-a07f-7a8bd51f527a", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Over 21 (Sim45)", task: "Over 21", "id":"71f7aca1-0092-49e5-96cc-3540a2d7adff", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0},
	{"label":"Approve customer (Sim46)", task: "Approve customer", "id":"2ee2afd0-8b99-429f-a54d-17ee3aece6e7", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Under 21 (Sim46)", task: "Under 21", "id":"0db795f1-493f-412f-934c-4959a2959846", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0},
	{"label":"Approve customer (Sim47)", task: "Approve customer", "id":"5733638d-49ab-4851-9f60-3e409fe4903d", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Under 21 (Sim47)", task: "Under 21", "id":"e9842d39-7c82-453f-b8aa-1a6339720c12", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0},
	{"label":"Approve customer (Sim48)", task: "Approve customer", "id":"5d8dd67e-d1e0-4d57-b536-f3fe550ec4cd", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Over 21 (Sim48)", task: "Over 21", "id":"72d06646-427a-4322-8d01-12083fc4d9bd", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0},
	{"label":"Approve customer (Sim49)", task: "Approve customer", "id":"1ee8f938-7bb2-40b2-ab98-01815a41963b", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Over 21 (Sim49)", task: "Over 21", "id":"f9d6976a-f81b-4a71-9526-39083b813230", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0},
	{"label":"Approve customer (Sim50)", task: "Approve customer", "id":"54261afe-ec9d-4bbb-abbe-f30060adc222", "starting_time": 2000, "ending_time": 2005, delay: 0, cost: 0},
	{"label":"Under 21 (Sim50)", task: "Under 21", "id":"d9903b3b-4531-449c-8ebd-5467c3c2b047", "starting_time": 2010, "ending_time": 2015, delay: 0, cost: 0},
	{"label":"Approve customer (Sim51)", task: "Approve customer", "id":"50a7927f-39eb-44ea-acc7-03bb6e447a87", "starting_time": 2040, "ending_time": 2045, delay: 0, cost: 0},
	{"label":"Under 21 (Sim51)", task: "Under 21", "id":"9b8f237e-ea7a-483f-9dcd-65aea070b5dc", "starting_time": 2050, "ending_time": 2055, delay: 0, cost: 0},
	{"label":"Approve customer (Sim52)", task: "Approve customer", "id":"860eb488-450a-4467-b282-a72716a3f0ac", "starting_time": 2080, "ending_time": 2085, delay: 0, cost: 0},
	{"label":"Over 21 (Sim52)", task: "Over 21", "id":"ea2bc7e7-51d0-44d5-998b-18361fe90dca", "starting_time": 2090, "ending_time": 2095, delay: 0, cost: 0},
	{"label":"Approve customer (Sim53)", task: "Approve customer", "id":"42591d04-96b7-4a88-bd15-19e510f82acb", "starting_time": 2120, "ending_time": 2125, delay: 0, cost: 0},
	{"label":"Over 21 (Sim53)", task: "Over 21", "id":"1aee5517-84b4-4a07-a9bb-fc478b684076", "starting_time": 2130, "ending_time": 2135, delay: 0, cost: 0},
	{"label":"Approve customer (Sim54)", task: "Approve customer", "id":"bb29a21b-39b1-4229-8e3d-0ae8f2153205", "starting_time": 2160, "ending_time": 2165, delay: 0, cost: 0},
	{"label":"Over 21 (Sim54)", task: "Over 21", "id":"18c36191-0e8c-426d-9fa7-3d3c1863bcc8", "starting_time": 2170, "ending_time": 2175, delay: 0, cost: 0},
	{"label":"Approve customer (Sim55)", task: "Approve customer", "id":"6b5cafb1-76ba-4c7f-aed0-f1c30351e865", "starting_time": 2200, "ending_time": 2205, delay: 0, cost: 0},
	{"label":"Over 21 (Sim55)", task: "Over 21", "id":"d30598f4-280c-46bf-b74c-e8cf5985e32b", "starting_time": 2210, "ending_time": 2215, delay: 0, cost: 0},
	{"label":"Approve customer (Sim56)", task: "Approve customer", "id":"6ea76302-3a57-42fa-85de-2912ab3d0703", "starting_time": 2240, "ending_time": 2245, delay: 0, cost: 0},
	{"label":"Over 21 (Sim56)", task: "Over 21", "id":"5d79237b-12ca-4367-ad65-e5eaf08f8e6a", "starting_time": 2250, "ending_time": 2255, delay: 0, cost: 0},
	{"label":"Approve customer (Sim57)", task: "Approve customer", "id":"61c1cc87-1617-496e-b91a-8b90811c85d0", "starting_time": 2280, "ending_time": 2285, delay: 0, cost: 0},
	{"label":"Under 21 (Sim57)", task: "Under 21", "id":"af607f79-300b-4eb5-b059-42ee17dc9acc", "starting_time": 2290, "ending_time": 2295, delay: 0, cost: 0},
	{"label":"Approve customer (Sim58)", task: "Approve customer", "id":"0431ca81-7ad5-4832-aa6b-2e32dd9ee444", "starting_time": 2320, "ending_time": 2325, delay: 0, cost: 0},
	{"label":"Over 21 (Sim58)", task: "Over 21", "id":"5975d67f-daeb-4b1e-815f-40e2b80891ab", "starting_time": 2330, "ending_time": 2335, delay: 0, cost: 0},
	{"label":"Approve customer (Sim59)", task: "Approve customer", "id":"96b16680-7317-4094-bec5-bf43fe37fcd0", "starting_time": 2360, "ending_time": 2365, delay: 0, cost: 0},
	{"label":"Over 21 (Sim59)", task: "Over 21", "id":"dde9d5f1-e138-41f3-945a-257a4e322c44", "starting_time": 2370, "ending_time": 2375, delay: 0, cost: 0},
	{"label":"Approve customer (Sim60)", task: "Approve customer", "id":"abe3ff8a-9690-43a8-8548-9519eea70163", "starting_time": 2400, "ending_time": 2405, delay: 0, cost: 0},
	{"label":"Over 21 (Sim60)", task: "Over 21", "id":"6df428db-6a44-4607-a002-96d73267f7fd", "starting_time": 2410, "ending_time": 2415, delay: 0, cost: 0},
	{"label":"Approve customer (Sim61)", task: "Approve customer", "id":"4a0812e8-e249-411f-848f-3963bf1db301", "starting_time": 2440, "ending_time": 2445, delay: 0, cost: 0},
	{"label":"Under 21 (Sim61)", task: "Under 21", "id":"0d9e3574-fe06-4941-9738-fef232ffeb00", "starting_time": 2450, "ending_time": 2455, delay: 0, cost: 0},
	{"label":"Approve customer (Sim62)", task: "Approve customer", "id":"f689ad94-1d06-4e3e-9d85-ce263406ea66", "starting_time": 2480, "ending_time": 2485, delay: 0, cost: 0},
	{"label":"Under 21 (Sim62)", task: "Under 21", "id":"57a780d1-55bc-41f1-8a75-ec07972b6cb5", "starting_time": 2490, "ending_time": 2495, delay: 0, cost: 0},
	{"label":"Approve customer (Sim63)", task: "Approve customer", "id":"966fea57-ee01-4ff3-aacb-63e551a3b307", "starting_time": 2520, "ending_time": 2525, delay: 0, cost: 0},
	{"label":"Under 21 (Sim63)", task: "Under 21", "id":"53c951fb-c41e-4309-9259-6af13ad3dd53", "starting_time": 2530, "ending_time": 2535, delay: 0, cost: 0},
	{"label":"Approve customer (Sim64)", task: "Approve customer", "id":"46cd295e-e979-46ed-b813-71f47fb98884", "starting_time": 2560, "ending_time": 2565, delay: 0, cost: 0},
	{"label":"Over 21 (Sim64)", task: "Over 21", "id":"f5c7f026-c50c-4506-9037-6776e53e535b", "starting_time": 2570, "ending_time": 2575, delay: 0, cost: 0},
	{"label":"Approve customer (Sim65)", task: "Approve customer", "id":"5cd67dff-dbda-4d11-8579-5ba79f0b28a8", "starting_time": 2600, "ending_time": 2605, delay: 0, cost: 0},
	{"label":"Over 21 (Sim65)", task: "Over 21", "id":"1ab06e39-7d5b-4b09-8116-0bce124f2994", "starting_time": 2610, "ending_time": 2615, delay: 0, cost: 0},
	{"label":"Approve customer (Sim66)", task: "Approve customer", "id":"cc9548c4-02e5-4461-9925-ce191243e284", "starting_time": 2640, "ending_time": 2645, delay: 0, cost: 0},
	{"label":"Under 21 (Sim66)", task: "Under 21", "id":"050a8cac-ab36-419e-8a5a-a3cd29b67a77", "starting_time": 2650, "ending_time": 2655, delay: 0, cost: 0},
	{"label":"Approve customer (Sim67)", task: "Approve customer", "id":"58df7f33-7d5d-4dd2-b2f6-12af9102ce80", "starting_time": 2680, "ending_time": 2685, delay: 0, cost: 0},
	{"label":"Under 21 (Sim67)", task: "Under 21", "id":"3f7fcf7b-b18e-451b-b9f6-764b660a9cce", "starting_time": 2690, "ending_time": 2695, delay: 0, cost: 0},
	{"label":"Approve customer (Sim68)", task: "Approve customer", "id":"fb07e38c-e051-4fa7-98ec-a420a65c2968", "starting_time": 2720, "ending_time": 2725, delay: 0, cost: 0},
	{"label":"Over 21 (Sim68)", task: "Over 21", "id":"4323adf3-f3da-4fb2-903d-7095767fb2e9", "starting_time": 2730, "ending_time": 2735, delay: 0, cost: 0},
	{"label":"Approve customer (Sim69)", task: "Approve customer", "id":"5a2ffb3f-8dd8-49f7-85cc-cd8fca88ff1b", "starting_time": 2760, "ending_time": 2765, delay: 0, cost: 0},
	{"label":"Under 21 (Sim69)", task: "Under 21", "id":"ec5f34a9-e2f4-4117-add5-9a122d40b903", "starting_time": 2770, "ending_time": 2775, delay: 0, cost: 0},
	{"label":"Approve customer (Sim70)", task: "Approve customer", "id":"4763719f-71f1-4929-8429-3bfabeb5a2dd", "starting_time": 2800, "ending_time": 2805, delay: 0, cost: 0},
	{"label":"Under 21 (Sim70)", task: "Under 21", "id":"b865f2d4-c4f5-48a6-a4b2-fb25c3e6e0b8", "starting_time": 2810, "ending_time": 2815, delay: 0, cost: 0},
	{"label":"Approve customer (Sim71)", task: "Approve customer", "id":"ddbb7f08-8ae7-45d2-9ecd-5dba5fabb991", "starting_time": 2840, "ending_time": 2845, delay: 0, cost: 0},
	{"label":"Over 21 (Sim71)", task: "Over 21", "id":"aecf5934-68b2-4247-b8d4-72d661575f2b", "starting_time": 2850, "ending_time": 2855, delay: 0, cost: 0},
	{"label":"Approve customer (Sim72)", task: "Approve customer", "id":"ff51d378-754e-4fbd-8a98-1a09db7f6b05", "starting_time": 2880, "ending_time": 2885, delay: 0, cost: 0},
	{"label":"Under 21 (Sim72)", task: "Under 21", "id":"2cf197c5-3207-4b64-9d65-7ff2629638a4", "starting_time": 2890, "ending_time": 2895, delay: 0, cost: 0},
	{"label":"Approve customer (Sim73)", task: "Approve customer", "id":"8aa9e55d-d797-4c7d-abc3-18ad9879d34b", "starting_time": 2920, "ending_time": 2925, delay: 0, cost: 0},
	{"label":"Over 21 (Sim73)", task: "Over 21", "id":"9e7bc6a9-44b6-4527-a833-6e0cc3c817fd", "starting_time": 2930, "ending_time": 2935, delay: 0, cost: 0},
	{"label":"Approve customer (Sim74)", task: "Approve customer", "id":"241eca03-01d4-4194-b2c4-b4b23d6198f5", "starting_time": 2960, "ending_time": 2965, delay: 0, cost: 0},
	{"label":"Over 21 (Sim74)", task: "Over 21", "id":"c715ee46-d3e7-4086-8a8d-7e4d74208ada", "starting_time": 2970, "ending_time": 2975, delay: 0, cost: 0},
	{"label":"Approve customer (Sim75)", task: "Approve customer", "id":"781be68a-3ee4-4bc1-9076-876c25698582", "starting_time": 3000, "ending_time": 3005, delay: 0, cost: 0},
	{"label":"Over 21 (Sim75)", task: "Over 21", "id":"6cd04dfd-9c8b-4088-9013-d80f38555907", "starting_time": 3010, "ending_time": 3015, delay: 0, cost: 0},
	{"label":"Approve customer (Sim76)", task: "Approve customer", "id":"fa4a0e89-14b0-4def-b0bf-373b1bebda4c", "starting_time": 3040, "ending_time": 3045, delay: 0, cost: 0},
	{"label":"Over 21 (Sim76)", task: "Over 21", "id":"6486c19b-0d95-4824-8cbe-f5b079bf3a8c", "starting_time": 3050, "ending_time": 3055, delay: 0, cost: 0},
	{"label":"Approve customer (Sim77)", task: "Approve customer", "id":"3e1394f9-a5c0-4d1f-93f7-52e65346fcef", "starting_time": 3080, "ending_time": 3085, delay: 0, cost: 0},
	{"label":"Over 21 (Sim77)", task: "Over 21", "id":"941a7e02-4879-4312-aeff-33b20ae2e172", "starting_time": 3090, "ending_time": 3095, delay: 0, cost: 0},
	{"label":"Approve customer (Sim78)", task: "Approve customer", "id":"e1f027d3-e58a-4250-919b-9c276664a47f", "starting_time": 3120, "ending_time": 3125, delay: 0, cost: 0},
	{"label":"Over 21 (Sim78)", task: "Over 21", "id":"f440bae1-f1ca-4e5f-861a-c64fd510b147", "starting_time": 3130, "ending_time": 3135, delay: 0, cost: 0},
	{"label":"Approve customer (Sim79)", task: "Approve customer", "id":"f413998a-5a79-42e4-ab86-953f5312cc00", "starting_time": 3160, "ending_time": 3165, delay: 0, cost: 0},
	{"label":"Over 21 (Sim79)", task: "Over 21", "id":"739051fc-8949-413f-9d4e-8a06f09cd307", "starting_time": 3170, "ending_time": 3175, delay: 0, cost: 0},
	{"label":"Approve customer (Sim80)", task: "Approve customer", "id":"3a37d023-13c3-42de-a205-92d982b238fd", "starting_time": 3200, "ending_time": 3205, delay: 0, cost: 0},
	{"label":"Over 21 (Sim80)", task: "Over 21", "id":"6eab2c5a-ba3a-4df1-922a-f02c811f839f", "starting_time": 3210, "ending_time": 3215, delay: 0, cost: 0},
	{"label":"Approve customer (Sim81)", task: "Approve customer", "id":"c89ed0f2-b428-4097-90f3-9980b75a1d1b", "starting_time": 3240, "ending_time": 3245, delay: 0, cost: 0},
	{"label":"Over 21 (Sim81)", task: "Over 21", "id":"9f6d9ab2-bc4b-449f-be1f-395f4433128b", "starting_time": 3250, "ending_time": 3255, delay: 0, cost: 0},
	{"label":"Approve customer (Sim82)", task: "Approve customer", "id":"17df2c0d-bc66-4450-918b-a833b3487479", "starting_time": 3280, "ending_time": 3285, delay: 0, cost: 0},
	{"label":"Under 21 (Sim82)", task: "Under 21", "id":"7c69e10a-fbee-4874-a725-5ff9d4becce2", "starting_time": 3290, "ending_time": 3295, delay: 0, cost: 0},
	{"label":"Approve customer (Sim83)", task: "Approve customer", "id":"70cc09e5-f8c1-4bb6-95dc-f6ded78e6c78", "starting_time": 3320, "ending_time": 3325, delay: 0, cost: 0},
	{"label":"Under 21 (Sim83)", task: "Under 21", "id":"a95b9203-f68b-453b-980c-301392a04678", "starting_time": 3330, "ending_time": 3335, delay: 0, cost: 0},
	{"label":"Approve customer (Sim84)", task: "Approve customer", "id":"7ceca141-d4a0-4867-971b-cd953365ad34", "starting_time": 3360, "ending_time": 3365, delay: 0, cost: 0},
	{"label":"Under 21 (Sim84)", task: "Under 21", "id":"ecc9f158-510f-420c-8665-ef567f04127e", "starting_time": 3370, "ending_time": 3375, delay: 0, cost: 0},
	{"label":"Approve customer (Sim85)", task: "Approve customer", "id":"6f20d839-00b1-426a-9336-3fb71292e3b1", "starting_time": 3400, "ending_time": 3405, delay: 0, cost: 0},
	{"label":"Over 21 (Sim85)", task: "Over 21", "id":"11ac4fa7-8b1b-46bf-8304-e2876d8276e5", "starting_time": 3410, "ending_time": 3415, delay: 0, cost: 0},
	{"label":"Approve customer (Sim86)", task: "Approve customer", "id":"d8787ad4-4d47-422b-a46c-c564cd9d8017", "starting_time": 3440, "ending_time": 3445, delay: 0, cost: 0},
	{"label":"Under 21 (Sim86)", task: "Under 21", "id":"d90f45cd-b27a-4548-89d2-7eee85cc9ede", "starting_time": 3450, "ending_time": 3455, delay: 0, cost: 0},
	{"label":"Approve customer (Sim87)", task: "Approve customer", "id":"674009c3-8a69-4c6f-8923-8c5e54ac1aad", "starting_time": 3480, "ending_time": 3485, delay: 0, cost: 0},
	{"label":"Over 21 (Sim87)", task: "Over 21", "id":"12e2e93c-4dd1-4849-bd7c-848d45cb2b90", "starting_time": 3490, "ending_time": 3495, delay: 0, cost: 0},
	{"label":"Approve customer (Sim88)", task: "Approve customer", "id":"177f318c-6ed7-4e43-acf7-f4956d6e14fe", "starting_time": 3520, "ending_time": 3525, delay: 0, cost: 0},
	{"label":"Under 21 (Sim88)", task: "Under 21", "id":"d050e999-aef6-43b1-9c5d-c652dc8f2ff2", "starting_time": 3530, "ending_time": 3535, delay: 0, cost: 0},
	{"label":"Approve customer (Sim89)", task: "Approve customer", "id":"d292d71a-e27d-425a-8fb3-60a50d51769f", "starting_time": 3560, "ending_time": 3565, delay: 0, cost: 0},
	{"label":"Over 21 (Sim89)", task: "Over 21", "id":"8610eb3c-fc58-4e22-a3dd-15fa516156b7", "starting_time": 3570, "ending_time": 3575, delay: 0, cost: 0},
	{"label":"Approve customer (Sim90)", task: "Approve customer", "id":"b64ace3f-688a-43a4-a7dc-811d79ff165d", "starting_time": 3600, "ending_time": 3605, delay: 0, cost: 0},
	{"label":"Over 21 (Sim90)", task: "Over 21", "id":"fbec4bcc-29bc-4435-b691-838197e6663f", "starting_time": 3610, "ending_time": 3615, delay: 0, cost: 0},
	{"label":"Approve customer (Sim91)", task: "Approve customer", "id":"722c8371-4cb7-4604-8ad2-36f5239c8279", "starting_time": 3640, "ending_time": 3645, delay: 0, cost: 0},
	{"label":"Under 21 (Sim91)", task: "Under 21", "id":"e11bfc86-7371-4e97-828e-88438182d7b0", "starting_time": 3650, "ending_time": 3655, delay: 0, cost: 0},
	{"label":"Approve customer (Sim92)", task: "Approve customer", "id":"9f5e31cd-746c-47d3-acfb-867ee6f40dfa", "starting_time": 3680, "ending_time": 3685, delay: 0, cost: 0},
	{"label":"Under 21 (Sim92)", task: "Under 21", "id":"ef7568fe-3f3f-48ae-b869-2ef15485f072", "starting_time": 3690, "ending_time": 3695, delay: 0, cost: 0},
	{"label":"Approve customer (Sim93)", task: "Approve customer", "id":"167efc49-b5ee-47a7-9836-f6e1f065841c", "starting_time": 3720, "ending_time": 3725, delay: 0, cost: 0},
	{"label":"Under 21 (Sim93)", task: "Under 21", "id":"9fbbe289-f5e1-40a3-a214-911364a0cecb", "starting_time": 3730, "ending_time": 3735, delay: 0, cost: 0},
	{"label":"Approve customer (Sim94)", task: "Approve customer", "id":"5d4ae366-9a34-4c4c-8d47-d825ea321e3f", "starting_time": 3760, "ending_time": 3765, delay: 0, cost: 0},
	{"label":"Over 21 (Sim94)", task: "Over 21", "id":"4ddf42e3-63cd-4abe-8ac0-8165fc1e9bba", "starting_time": 3770, "ending_time": 3775, delay: 0, cost: 0},
	{"label":"Approve customer (Sim95)", task: "Approve customer", "id":"f7bf53d9-c6c2-4b06-8ec9-e74559e509b1", "starting_time": 3800, "ending_time": 3805, delay: 0, cost: 0},
	{"label":"Over 21 (Sim95)", task: "Over 21", "id":"75c6d820-6187-4807-a770-ee48228fb063", "starting_time": 3810, "ending_time": 3815, delay: 0, cost: 0},
	{"label":"Approve customer (Sim96)", task: "Approve customer", "id":"6569bb78-5ab6-4460-b6c6-cc677c2becd2", "starting_time": 3840, "ending_time": 3845, delay: 0, cost: 0},
	{"label":"Under 21 (Sim96)", task: "Under 21", "id":"730a193a-5922-4a56-9c85-8b10b625d8ea", "starting_time": 3850, "ending_time": 3855, delay: 0, cost: 0},
	{"label":"Approve customer (Sim97)", task: "Approve customer", "id":"65f38376-6232-402b-a6e3-7c18b67cce13", "starting_time": 3880, "ending_time": 3885, delay: 0, cost: 0},
	{"label":"Under 21 (Sim97)", task: "Under 21", "id":"17d54e67-da76-473b-bb93-bf21489d33af", "starting_time": 3890, "ending_time": 3895, delay: 0, cost: 0},
	{"label":"Approve customer (Sim98)", task: "Approve customer", "id":"46654b25-67ec-4e5f-8b02-5620220a0504", "starting_time": 3920, "ending_time": 3925, delay: 0, cost: 0},
	{"label":"Over 21 (Sim98)", task: "Over 21", "id":"644aebb6-431c-4c91-8440-6960820acb9f", "starting_time": 3930, "ending_time": 3935, delay: 0, cost: 0},
	{"label":"Approve customer (Sim99)", task: "Approve customer", "id":"5f9dea3f-75e6-43fc-b021-509dea4712a1", "starting_time": 3960, "ending_time": 3965, delay: 0, cost: 0},
	{"label":"Over 21 (Sim99)", task: "Over 21", "id":"2ba2a2ad-8b22-492b-a033-4f692a173f03", "starting_time": 3970, "ending_time": 3975, delay: 0, cost: 0}
]},
{label: "r2", times: [
	{"label":"Validate customer (Sim0)", task: "Validate customer", "id":"4ab70b9b-b543-41ce-8135-532c9424e433", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Validate customer (Sim1)", task: "Validate customer", "id":"272c326d-ff8d-4630-9302-c7c1e418acbc", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Validate customer (Sim2)", task: "Validate customer", "id":"03569124-a061-4973-99b7-f360fcbba669", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Validate customer (Sim3)", task: "Validate customer", "id":"2d153e2f-ce7d-4e16-a625-8afc3a9ba203", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Validate customer (Sim4)", task: "Validate customer", "id":"4ec66eb3-8b9b-44df-989a-5ddc9eb6cce2", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Validate customer (Sim5)", task: "Validate customer", "id":"94a92160-433f-4b36-b95c-42b50be3e9eb", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Validate customer (Sim6)", task: "Validate customer", "id":"54cfead8-54fa-4d00-bfa1-371cd4487216", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Validate customer (Sim7)", task: "Validate customer", "id":"f661e070-3b86-4a24-ada6-f8297d335c61", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Validate customer (Sim8)", task: "Validate customer", "id":"b5e585b3-a560-4cab-b0d8-1c3332071b4e", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Validate customer (Sim9)", task: "Validate customer", "id":"3017e6b7-ea16-43e5-ab59-395353db8996", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Validate customer (Sim10)", task: "Validate customer", "id":"ac6e7ae6-bfdc-4286-a597-1c8de4c5bfa5", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Validate customer (Sim11)", task: "Validate customer", "id":"02bc057a-6d45-418e-a65e-49d68ef5ed37", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Validate customer (Sim12)", task: "Validate customer", "id":"d28e8f3b-ad16-43e3-8418-48cb81ee9f93", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Validate customer (Sim13)", task: "Validate customer", "id":"5e77a9fc-3db5-4b1c-a6a6-3b8bc245908c", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Validate customer (Sim14)", task: "Validate customer", "id":"bb1ce998-7781-4376-96a5-f9bbc3241414", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Validate customer (Sim15)", task: "Validate customer", "id":"52811189-ba31-4450-b01d-1185f70e6ce9", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Validate customer (Sim16)", task: "Validate customer", "id":"f01bed18-0ade-4055-acda-e173d9031ade", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Validate customer (Sim17)", task: "Validate customer", "id":"98bc4637-447c-4e29-affa-f6eb01ae1b7c", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Validate customer (Sim18)", task: "Validate customer", "id":"72c44079-323a-4d94-b326-95ed8dfa215a", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Validate customer (Sim19)", task: "Validate customer", "id":"39b76593-a260-4bdf-88ed-3ab867a01a67", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Validate customer (Sim20)", task: "Validate customer", "id":"2e6d804b-1008-49a3-a7ea-235d8c8b389e", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Validate customer (Sim21)", task: "Validate customer", "id":"cf79614f-53cf-491f-8146-ca0eabae8ca0", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Validate customer (Sim22)", task: "Validate customer", "id":"2e93158c-857f-4583-bb2f-a4cd4e66e61d", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Validate customer (Sim23)", task: "Validate customer", "id":"ef52a51b-ea7b-4860-ab06-db7172106409", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Validate customer (Sim24)", task: "Validate customer", "id":"b4eb5aa0-645f-4e73-9e1e-24ab5e7e78cb", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Validate customer (Sim25)", task: "Validate customer", "id":"ee0f4b9c-528d-4259-ac58-1def6d5afaf1", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Validate customer (Sim26)", task: "Validate customer", "id":"4cf9c30b-57ea-467c-9574-28677e2c399f", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Validate customer (Sim27)", task: "Validate customer", "id":"542c2497-e29a-4001-8ffc-175cd34d676b", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Validate customer (Sim28)", task: "Validate customer", "id":"bb02276d-5e45-4ac8-abf9-fa034870d32a", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Validate customer (Sim29)", task: "Validate customer", "id":"99a3ccd1-0708-4932-9c46-6ef74afe14cf", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Validate customer (Sim30)", task: "Validate customer", "id":"a48c3216-5c04-4c76-8ba5-d57f729dd3e3", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Validate customer (Sim31)", task: "Validate customer", "id":"1f55074a-d2ef-45d9-9b45-fb0760be09c0", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Validate customer (Sim32)", task: "Validate customer", "id":"fac4077f-653a-4174-9a33-39e80929b531", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Validate customer (Sim33)", task: "Validate customer", "id":"751663bb-0d9f-43e6-ada4-3041053436fa", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Validate customer (Sim34)", task: "Validate customer", "id":"a003d528-3a77-457a-b28c-63154734f4b6", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Validate customer (Sim35)", task: "Validate customer", "id":"2ebfe6df-3cd8-40ad-a3e7-0064256d896c", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Validate customer (Sim36)", task: "Validate customer", "id":"032c5426-73d2-4817-9ecb-be5e82b7a1a7", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Validate customer (Sim37)", task: "Validate customer", "id":"7996c821-50a5-4a9b-9a2a-2b6d95e2cc86", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Validate customer (Sim38)", task: "Validate customer", "id":"9841c911-1216-4898-bd13-52c94c135289", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Validate customer (Sim39)", task: "Validate customer", "id":"2cbc8a9f-afa5-4363-981a-aadac17f748b", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Validate customer (Sim40)", task: "Validate customer", "id":"401d2ce1-15e5-458e-99fd-55f3f50ad238", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Validate customer (Sim41)", task: "Validate customer", "id":"e7fc5ab4-5172-447d-a23c-bafe2b6483d9", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Validate customer (Sim42)", task: "Validate customer", "id":"171e1bdf-a598-402d-9e65-102db4254d65", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Validate customer (Sim43)", task: "Validate customer", "id":"74eac729-5a8a-4fbb-a19c-f4a06f2cba13", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Validate customer (Sim44)", task: "Validate customer", "id":"03aca844-f0a3-499f-b865-81c36ad9f6ae", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Validate customer (Sim45)", task: "Validate customer", "id":"855285cd-4d4e-4b8a-8a37-72a7d9c66e55", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Validate customer (Sim46)", task: "Validate customer", "id":"52d17117-d628-49c8-8e33-fc09c0194142", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Validate customer (Sim47)", task: "Validate customer", "id":"3c6a4a82-e751-4fa5-81ed-23355f15f0e7", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Validate customer (Sim48)", task: "Validate customer", "id":"0057fe20-d9ac-4d24-b420-54163ceeba1f", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Validate customer (Sim49)", task: "Validate customer", "id":"c1596eb4-79a0-4c83-bc7d-c8b6f2708ad1", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Validate customer (Sim50)", task: "Validate customer", "id":"c8b8f345-a86b-4c33-95cd-5dc8f7dff750", "starting_time": 2005, "ending_time": 2010, delay: 0, cost: 0},
	{"label":"Validate customer (Sim51)", task: "Validate customer", "id":"332ee333-2407-4e69-8fd1-296ab25c00cf", "starting_time": 2045, "ending_time": 2050, delay: 0, cost: 0},
	{"label":"Validate customer (Sim52)", task: "Validate customer", "id":"a4d7adfe-7251-496c-9d1e-2e321a21aea6", "starting_time": 2085, "ending_time": 2090, delay: 0, cost: 0},
	{"label":"Validate customer (Sim53)", task: "Validate customer", "id":"b3417d46-1a59-45b5-b16c-10f057d9acf5", "starting_time": 2125, "ending_time": 2130, delay: 0, cost: 0},
	{"label":"Validate customer (Sim54)", task: "Validate customer", "id":"31b04482-c4e4-4b22-8889-68598b91cede", "starting_time": 2165, "ending_time": 2170, delay: 0, cost: 0},
	{"label":"Validate customer (Sim55)", task: "Validate customer", "id":"c0401145-5e7c-4dc7-8d2b-b63163a91040", "starting_time": 2205, "ending_time": 2210, delay: 0, cost: 0},
	{"label":"Validate customer (Sim56)", task: "Validate customer", "id":"ae6a9105-4b1f-4f83-946d-315e3c4a05bb", "starting_time": 2245, "ending_time": 2250, delay: 0, cost: 0},
	{"label":"Validate customer (Sim57)", task: "Validate customer", "id":"81b14325-648b-4f39-bba5-0f7f0127ef46", "starting_time": 2285, "ending_time": 2290, delay: 0, cost: 0},
	{"label":"Validate customer (Sim58)", task: "Validate customer", "id":"282c95ec-b425-4a69-a163-a738b40993a5", "starting_time": 2325, "ending_time": 2330, delay: 0, cost: 0},
	{"label":"Validate customer (Sim59)", task: "Validate customer", "id":"f1a6284a-bd97-430b-8cdb-b8d631df56ca", "starting_time": 2365, "ending_time": 2370, delay: 0, cost: 0},
	{"label":"Validate customer (Sim60)", task: "Validate customer", "id":"9b318ac8-e82b-4b20-9db2-a5c080c8428c", "starting_time": 2405, "ending_time": 2410, delay: 0, cost: 0},
	{"label":"Validate customer (Sim61)", task: "Validate customer", "id":"be08048e-f1a4-432c-9326-910486c7d36b", "starting_time": 2445, "ending_time": 2450, delay: 0, cost: 0},
	{"label":"Validate customer (Sim62)", task: "Validate customer", "id":"7ea077e5-29e7-4975-8ee4-5685a7d19f15", "starting_time": 2485, "ending_time": 2490, delay: 0, cost: 0},
	{"label":"Validate customer (Sim63)", task: "Validate customer", "id":"50fc0c0b-fbeb-470d-b251-35dbcd594167", "starting_time": 2525, "ending_time": 2530, delay: 0, cost: 0},
	{"label":"Validate customer (Sim64)", task: "Validate customer", "id":"a756ee3e-4dc9-4472-b229-69db13c70595", "starting_time": 2565, "ending_time": 2570, delay: 0, cost: 0},
	{"label":"Validate customer (Sim65)", task: "Validate customer", "id":"f1c93cf5-17ba-402f-8636-fd1007a4f404", "starting_time": 2605, "ending_time": 2610, delay: 0, cost: 0},
	{"label":"Validate customer (Sim66)", task: "Validate customer", "id":"276a2079-2285-481f-af36-3a91f7ef9c90", "starting_time": 2645, "ending_time": 2650, delay: 0, cost: 0},
	{"label":"Validate customer (Sim67)", task: "Validate customer", "id":"0259966c-1c51-4b85-ab1e-e9245b14917d", "starting_time": 2685, "ending_time": 2690, delay: 0, cost: 0},
	{"label":"Validate customer (Sim68)", task: "Validate customer", "id":"cd3d12cd-45a2-4526-9a4e-1b7cbf8092dc", "starting_time": 2725, "ending_time": 2730, delay: 0, cost: 0},
	{"label":"Validate customer (Sim69)", task: "Validate customer", "id":"c1a608d3-511e-422f-96ed-315a8b649394", "starting_time": 2765, "ending_time": 2770, delay: 0, cost: 0},
	{"label":"Validate customer (Sim70)", task: "Validate customer", "id":"75f1ab15-cac0-44de-a286-2a6df8832de3", "starting_time": 2805, "ending_time": 2810, delay: 0, cost: 0},
	{"label":"Validate customer (Sim71)", task: "Validate customer", "id":"52fce777-4308-4114-8aaf-def9c9425128", "starting_time": 2845, "ending_time": 2850, delay: 0, cost: 0},
	{"label":"Validate customer (Sim72)", task: "Validate customer", "id":"0b7f8f25-e4cf-4a9a-8daa-1272444e433f", "starting_time": 2885, "ending_time": 2890, delay: 0, cost: 0},
	{"label":"Validate customer (Sim73)", task: "Validate customer", "id":"94b536a3-74ea-4d87-8304-c42563b3dc3b", "starting_time": 2925, "ending_time": 2930, delay: 0, cost: 0},
	{"label":"Validate customer (Sim74)", task: "Validate customer", "id":"1fd084af-92e2-4e74-897a-9753b245c4b5", "starting_time": 2965, "ending_time": 2970, delay: 0, cost: 0},
	{"label":"Validate customer (Sim75)", task: "Validate customer", "id":"93cf7b81-ee18-4dda-a4a3-9055d1af208b", "starting_time": 3005, "ending_time": 3010, delay: 0, cost: 0},
	{"label":"Validate customer (Sim76)", task: "Validate customer", "id":"31eac421-24a8-4cc7-9cc8-c264eb27f3ea", "starting_time": 3045, "ending_time": 3050, delay: 0, cost: 0},
	{"label":"Validate customer (Sim77)", task: "Validate customer", "id":"d46d1dc0-fdc0-4849-84c4-5399d4e1cb13", "starting_time": 3085, "ending_time": 3090, delay: 0, cost: 0},
	{"label":"Validate customer (Sim78)", task: "Validate customer", "id":"dcbc3f93-fccc-4d94-b3f7-4ea076b15daa", "starting_time": 3125, "ending_time": 3130, delay: 0, cost: 0},
	{"label":"Validate customer (Sim79)", task: "Validate customer", "id":"514ad57b-8202-4452-82b6-c28753673c60", "starting_time": 3165, "ending_time": 3170, delay: 0, cost: 0},
	{"label":"Validate customer (Sim80)", task: "Validate customer", "id":"8aeb86c5-cee5-41a8-97e9-7a05a2fa0538", "starting_time": 3205, "ending_time": 3210, delay: 0, cost: 0},
	{"label":"Validate customer (Sim81)", task: "Validate customer", "id":"a957cb38-a14b-4be4-a0df-04ce1437f3bb", "starting_time": 3245, "ending_time": 3250, delay: 0, cost: 0},
	{"label":"Validate customer (Sim82)", task: "Validate customer", "id":"7f76e973-be1b-4597-8fc2-548bf5059a33", "starting_time": 3285, "ending_time": 3290, delay: 0, cost: 0},
	{"label":"Validate customer (Sim83)", task: "Validate customer", "id":"6bc23f83-7d61-4501-8a2d-5c7c5c6871f0", "starting_time": 3325, "ending_time": 3330, delay: 0, cost: 0},
	{"label":"Validate customer (Sim84)", task: "Validate customer", "id":"f6ff058c-8745-4233-ad69-a5451ef984b6", "starting_time": 3365, "ending_time": 3370, delay: 0, cost: 0},
	{"label":"Validate customer (Sim85)", task: "Validate customer", "id":"0cecf768-f591-452e-ab9f-1cde546f6fc7", "starting_time": 3405, "ending_time": 3410, delay: 0, cost: 0},
	{"label":"Validate customer (Sim86)", task: "Validate customer", "id":"757b2f4f-d1d4-455c-a168-1917bc93a034", "starting_time": 3445, "ending_time": 3450, delay: 0, cost: 0},
	{"label":"Validate customer (Sim87)", task: "Validate customer", "id":"98da80d6-9105-4ed7-a003-e19eba1043d1", "starting_time": 3485, "ending_time": 3490, delay: 0, cost: 0},
	{"label":"Validate customer (Sim88)", task: "Validate customer", "id":"c19188e7-8da9-44b7-8a3d-5aba8766f26c", "starting_time": 3525, "ending_time": 3530, delay: 0, cost: 0},
	{"label":"Validate customer (Sim89)", task: "Validate customer", "id":"638b30ea-2050-469c-b790-fd70b9eecebc", "starting_time": 3565, "ending_time": 3570, delay: 0, cost: 0},
	{"label":"Validate customer (Sim90)", task: "Validate customer", "id":"e8dc924a-d1bb-46a7-8c7a-24719da048b3", "starting_time": 3605, "ending_time": 3610, delay: 0, cost: 0},
	{"label":"Validate customer (Sim91)", task: "Validate customer", "id":"3cbed014-585a-4b06-bd17-940f7acc0c13", "starting_time": 3645, "ending_time": 3650, delay: 0, cost: 0},
	{"label":"Validate customer (Sim92)", task: "Validate customer", "id":"6126d21b-fe1c-4627-9c19-2ef5d529ff4d", "starting_time": 3685, "ending_time": 3690, delay: 0, cost: 0},
	{"label":"Validate customer (Sim93)", task: "Validate customer", "id":"705c2e72-939e-4722-b47d-22ce954b025f", "starting_time": 3725, "ending_time": 3730, delay: 0, cost: 0},
	{"label":"Validate customer (Sim94)", task: "Validate customer", "id":"d8c36607-260b-4099-9d4a-55b9b6bde33d", "starting_time": 3765, "ending_time": 3770, delay: 0, cost: 0},
	{"label":"Validate customer (Sim95)", task: "Validate customer", "id":"41ea79d5-bd83-488c-b2ee-e8c634c18e6e", "starting_time": 3805, "ending_time": 3810, delay: 0, cost: 0},
	{"label":"Validate customer (Sim96)", task: "Validate customer", "id":"cb29e592-3ba9-4746-9612-96916e8738be", "starting_time": 3845, "ending_time": 3850, delay: 0, cost: 0},
	{"label":"Validate customer (Sim97)", task: "Validate customer", "id":"11670644-29c9-4d51-820d-07a788ce443c", "starting_time": 3885, "ending_time": 3890, delay: 0, cost: 0},
	{"label":"Validate customer (Sim98)", task: "Validate customer", "id":"29b257bb-7ae1-463f-8cc7-f71aa751dfc1", "starting_time": 3925, "ending_time": 3930, delay: 0, cost: 0},
	{"label":"Validate customer (Sim99)", task: "Validate customer", "id":"8419fe8b-59ec-4658-84d1-f507b8731642", "starting_time": 3965, "ending_time": 3970, delay: 0, cost: 0}
]},
{label: "r3", times: [

]},
{label: "r4", times: [

]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Approve customer (Sim0)", task: "Approve customer", "id":"dd2184e2-22da-47ef-b40e-dcdc1a5ab605", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Validate customer (Sim0)", task: "Validate customer", "id":"4ab70b9b-b543-41ce-8135-532c9424e433", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Under 21 (Sim0)", task: "Under 21", "id":"b4e58c24-244c-4431-8d06-2d85ff851df4", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Approve customer (Sim1)", task: "Approve customer", "id":"6cf52202-852b-4cbb-ae00-afd3570b3729", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Validate customer (Sim1)", task: "Validate customer", "id":"272c326d-ff8d-4630-9302-c7c1e418acbc", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Over 21 (Sim1)", task: "Over 21", "id":"3d5c2871-6c98-4b6b-8d7b-aa6e42b19326", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Approve customer (Sim10)", task: "Approve customer", "id":"786b0ef6-3ba9-4cbf-84dc-373f5e44fafe", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Validate customer (Sim10)", task: "Validate customer", "id":"ac6e7ae6-bfdc-4286-a597-1c8de4c5bfa5", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Over 21 (Sim10)", task: "Over 21", "id":"93118742-d8c7-4818-92ab-653a514d3db3", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Approve customer (Sim11)", task: "Approve customer", "id":"e51ea4e9-e45e-442f-a8a6-0f3ac1855303", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Validate customer (Sim11)", task: "Validate customer", "id":"02bc057a-6d45-418e-a65e-49d68ef5ed37", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Under 21 (Sim11)", task: "Under 21", "id":"344d63b6-7139-49ad-a7b5-ccf9ddc5dc11", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Approve customer (Sim12)", task: "Approve customer", "id":"17800395-c6a7-4ad5-bcba-b13a064f063f", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Validate customer (Sim12)", task: "Validate customer", "id":"d28e8f3b-ad16-43e3-8418-48cb81ee9f93", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Under 21 (Sim12)", task: "Under 21", "id":"7ec11883-7e2a-4210-980b-6a50787a6923", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Approve customer (Sim13)", task: "Approve customer", "id":"fbfd6501-c509-40d2-9dad-aca8cef159c8", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Validate customer (Sim13)", task: "Validate customer", "id":"5e77a9fc-3db5-4b1c-a6a6-3b8bc245908c", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Over 21 (Sim13)", task: "Over 21", "id":"c3bc2565-f8a4-4208-afba-8778761ae794", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Approve customer (Sim14)", task: "Approve customer", "id":"1ddbd8c2-de7b-40b1-b3a9-561d3fc1940a", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Validate customer (Sim14)", task: "Validate customer", "id":"bb1ce998-7781-4376-96a5-f9bbc3241414", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Under 21 (Sim14)", task: "Under 21", "id":"81463d8d-3d23-4f2d-8076-9d38ab698a16", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Approve customer (Sim15)", task: "Approve customer", "id":"f693c29d-0b51-470b-97a5-ab31f6b33a31", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Validate customer (Sim15)", task: "Validate customer", "id":"52811189-ba31-4450-b01d-1185f70e6ce9", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Under 21 (Sim15)", task: "Under 21", "id":"7a5c7413-263a-4df6-bad3-afc1ab170ab9", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Approve customer (Sim16)", task: "Approve customer", "id":"fc14985d-e52c-4e0d-a0a6-6eeab5b741f7", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Validate customer (Sim16)", task: "Validate customer", "id":"f01bed18-0ade-4055-acda-e173d9031ade", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Under 21 (Sim16)", task: "Under 21", "id":"81ad03aa-f184-4af0-9444-ee473c6a13a9", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Approve customer (Sim17)", task: "Approve customer", "id":"f578d283-7fa6-4f4f-a9cf-0eb88a718b74", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Validate customer (Sim17)", task: "Validate customer", "id":"98bc4637-447c-4e29-affa-f6eb01ae1b7c", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Over 21 (Sim17)", task: "Over 21", "id":"327ddd0b-6df7-4046-badd-0903b639f583", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Approve customer (Sim18)", task: "Approve customer", "id":"db63d8a1-ab97-4890-b3ce-cefb4297add7", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Validate customer (Sim18)", task: "Validate customer", "id":"72c44079-323a-4d94-b326-95ed8dfa215a", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Under 21 (Sim18)", task: "Under 21", "id":"41c5de38-0206-4dda-9822-398183b52ac0", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Approve customer (Sim19)", task: "Approve customer", "id":"2454788f-71c3-4581-b7ee-a65b8433af20", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Validate customer (Sim19)", task: "Validate customer", "id":"39b76593-a260-4bdf-88ed-3ab867a01a67", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Under 21 (Sim19)", task: "Under 21", "id":"b426dd94-ec7c-4858-88d0-62a094abf860", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Approve customer (Sim2)", task: "Approve customer", "id":"2523f4fd-7d8b-4a4f-b647-0f319c84e9ae", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Validate customer (Sim2)", task: "Validate customer", "id":"03569124-a061-4973-99b7-f360fcbba669", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Under 21 (Sim2)", task: "Under 21", "id":"7bbf17e8-2ecd-43dd-bc13-efaf3cf6fa58", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Approve customer (Sim20)", task: "Approve customer", "id":"6656d034-c06b-41d2-8b6b-1e3bbf11050d", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Validate customer (Sim20)", task: "Validate customer", "id":"2e6d804b-1008-49a3-a7ea-235d8c8b389e", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Under 21 (Sim20)", task: "Under 21", "id":"2c0c68b2-8310-4c96-9b6e-5b620f674645", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Approve customer (Sim21)", task: "Approve customer", "id":"74f9d38f-ff64-439e-83cc-e8a367d40043", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Validate customer (Sim21)", task: "Validate customer", "id":"cf79614f-53cf-491f-8146-ca0eabae8ca0", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Under 21 (Sim21)", task: "Under 21", "id":"d55622fa-56d2-49e3-99ab-e75e336c25a6", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Approve customer (Sim22)", task: "Approve customer", "id":"573d4756-155d-4962-b453-ce5dd5d1fedd", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Validate customer (Sim22)", task: "Validate customer", "id":"2e93158c-857f-4583-bb2f-a4cd4e66e61d", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Under 21 (Sim22)", task: "Under 21", "id":"13b4efed-ca4f-469b-b6b9-6ac7e7a25947", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Approve customer (Sim23)", task: "Approve customer", "id":"15c6b84b-0cec-4a39-825c-76b9fb127f44", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Validate customer (Sim23)", task: "Validate customer", "id":"ef52a51b-ea7b-4860-ab06-db7172106409", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Over 21 (Sim23)", task: "Over 21", "id":"cce9f2b2-80c3-4c20-a983-0d71e5310a0c", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Approve customer (Sim24)", task: "Approve customer", "id":"c751f70e-20b5-45be-ad15-6fd12eb89a65", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Validate customer (Sim24)", task: "Validate customer", "id":"b4eb5aa0-645f-4e73-9e1e-24ab5e7e78cb", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Over 21 (Sim24)", task: "Over 21", "id":"66e06d02-a221-43f7-9cdf-5cc29b728cc1", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Approve customer (Sim25)", task: "Approve customer", "id":"09460a5a-7804-40ac-bdd7-29aa14f1420c", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Validate customer (Sim25)", task: "Validate customer", "id":"ee0f4b9c-528d-4259-ac58-1def6d5afaf1", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Over 21 (Sim25)", task: "Over 21", "id":"731c49b2-e6a3-4712-ba0b-64f0f0970880", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Approve customer (Sim26)", task: "Approve customer", "id":"35f62a3d-f17c-4809-8305-1d8f9498c06e", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Validate customer (Sim26)", task: "Validate customer", "id":"4cf9c30b-57ea-467c-9574-28677e2c399f", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Over 21 (Sim26)", task: "Over 21", "id":"930279d2-750f-4935-8580-486662772b73", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Approve customer (Sim27)", task: "Approve customer", "id":"7c98c1d3-f0db-40d0-98ce-ab20ddc16434", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Validate customer (Sim27)", task: "Validate customer", "id":"542c2497-e29a-4001-8ffc-175cd34d676b", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Under 21 (Sim27)", task: "Under 21", "id":"51bcf3a7-4dc8-4b0d-aa8b-431092952cdb", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Approve customer (Sim28)", task: "Approve customer", "id":"f2c94a55-3470-4282-baa5-1aa25c0f34f5", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Validate customer (Sim28)", task: "Validate customer", "id":"bb02276d-5e45-4ac8-abf9-fa034870d32a", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Under 21 (Sim28)", task: "Under 21", "id":"7fc599ac-147e-47b3-9a26-ca43de4d3a7c", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Approve customer (Sim29)", task: "Approve customer", "id":"37f7ebc1-9730-4eb0-9ca6-530934e39174", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Validate customer (Sim29)", task: "Validate customer", "id":"99a3ccd1-0708-4932-9c46-6ef74afe14cf", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Over 21 (Sim29)", task: "Over 21", "id":"ee6e632c-9020-4737-9b49-6d34d668597c", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Approve customer (Sim3)", task: "Approve customer", "id":"bb47f39d-65ad-4096-b208-8a28229356b9", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Validate customer (Sim3)", task: "Validate customer", "id":"2d153e2f-ce7d-4e16-a625-8afc3a9ba203", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Over 21 (Sim3)", task: "Over 21", "id":"7b733358-62bc-41db-a443-f5e0694adaa2", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Approve customer (Sim30)", task: "Approve customer", "id":"686dc138-d72d-4d6a-a889-ba4964810f65", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Validate customer (Sim30)", task: "Validate customer", "id":"a48c3216-5c04-4c76-8ba5-d57f729dd3e3", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Over 21 (Sim30)", task: "Over 21", "id":"9095fd84-2fd5-4f15-aa2a-2e1b62ca18f4", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Approve customer (Sim31)", task: "Approve customer", "id":"146bf8f0-e5f2-491b-a68b-dfc5d4d6727d", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Validate customer (Sim31)", task: "Validate customer", "id":"1f55074a-d2ef-45d9-9b45-fb0760be09c0", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Under 21 (Sim31)", task: "Under 21", "id":"83300975-928a-4a83-980c-19b0c1c30073", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Approve customer (Sim32)", task: "Approve customer", "id":"0d4535bb-b623-4bfc-a0f5-37cd1a62f0f0", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Validate customer (Sim32)", task: "Validate customer", "id":"fac4077f-653a-4174-9a33-39e80929b531", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Over 21 (Sim32)", task: "Over 21", "id":"c4bd4cf0-05de-429e-8728-b329bc49d306", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Approve customer (Sim33)", task: "Approve customer", "id":"e9f92338-9f8e-4287-9f16-a5aac1d718ec", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Validate customer (Sim33)", task: "Validate customer", "id":"751663bb-0d9f-43e6-ada4-3041053436fa", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Under 21 (Sim33)", task: "Under 21", "id":"954a9106-4bf1-4ed9-9f16-ba65545f55ec", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Approve customer (Sim34)", task: "Approve customer", "id":"9c026c41-918c-496a-b543-11d59256fea9", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Validate customer (Sim34)", task: "Validate customer", "id":"a003d528-3a77-457a-b28c-63154734f4b6", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Under 21 (Sim34)", task: "Under 21", "id":"25afae49-950b-4004-9101-44ea0cd7f428", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Approve customer (Sim35)", task: "Approve customer", "id":"ee0b0df7-fdeb-433b-b848-3bc2d40d3ae0", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Validate customer (Sim35)", task: "Validate customer", "id":"2ebfe6df-3cd8-40ad-a3e7-0064256d896c", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Under 21 (Sim35)", task: "Under 21", "id":"633fa1de-aab0-45cb-9430-371650a4faae", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Approve customer (Sim36)", task: "Approve customer", "id":"95f2d23b-efd6-4eb0-ba4f-05c3c0429ea6", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Validate customer (Sim36)", task: "Validate customer", "id":"032c5426-73d2-4817-9ecb-be5e82b7a1a7", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Over 21 (Sim36)", task: "Over 21", "id":"f3c3d55d-45ff-46cc-a292-d50893248844", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Approve customer (Sim37)", task: "Approve customer", "id":"0a61d62e-2af4-43f5-822a-8eefaa18d54e", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Validate customer (Sim37)", task: "Validate customer", "id":"7996c821-50a5-4a9b-9a2a-2b6d95e2cc86", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Under 21 (Sim37)", task: "Under 21", "id":"7c063329-0888-4632-a68d-30744864e205", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Approve customer (Sim38)", task: "Approve customer", "id":"ac92e8fd-5bca-42c4-bc78-a3a8ab6f6722", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Validate customer (Sim38)", task: "Validate customer", "id":"9841c911-1216-4898-bd13-52c94c135289", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Under 21 (Sim38)", task: "Under 21", "id":"69b1696d-803c-4eae-b311-3eeba5883038", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Approve customer (Sim39)", task: "Approve customer", "id":"d7f35d03-9183-440d-9fc5-66e6370c67cc", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Validate customer (Sim39)", task: "Validate customer", "id":"2cbc8a9f-afa5-4363-981a-aadac17f748b", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Under 21 (Sim39)", task: "Under 21", "id":"4750649b-3faf-4587-97fe-a3ccaf291668", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Approve customer (Sim4)", task: "Approve customer", "id":"8c38c789-d18b-4d9a-8746-dbadd6cbb995", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Validate customer (Sim4)", task: "Validate customer", "id":"4ec66eb3-8b9b-44df-989a-5ddc9eb6cce2", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Under 21 (Sim4)", task: "Under 21", "id":"b1ce505b-c17f-47c9-8d2c-3ad980e153b1", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Approve customer (Sim40)", task: "Approve customer", "id":"2f6b71c8-cc96-4a06-bff4-86f6725ebce5", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Validate customer (Sim40)", task: "Validate customer", "id":"401d2ce1-15e5-458e-99fd-55f3f50ad238", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Over 21 (Sim40)", task: "Over 21", "id":"5be9dc82-7df1-4efc-8de2-24022a443351", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Approve customer (Sim41)", task: "Approve customer", "id":"34ed80e6-17e8-416e-8eac-00ac051c1fd5", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Validate customer (Sim41)", task: "Validate customer", "id":"e7fc5ab4-5172-447d-a23c-bafe2b6483d9", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Under 21 (Sim41)", task: "Under 21", "id":"36171d25-13ab-4e73-b9b3-7ffe50afc863", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Approve customer (Sim42)", task: "Approve customer", "id":"69fcb050-951f-44e8-a6c9-74d3bb2f8271", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Validate customer (Sim42)", task: "Validate customer", "id":"171e1bdf-a598-402d-9e65-102db4254d65", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Over 21 (Sim42)", task: "Over 21", "id":"cee8affd-8bb9-40f3-a024-12b6d3318084", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Approve customer (Sim43)", task: "Approve customer", "id":"fcd990ba-b194-4f20-8ebb-c76130157472", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Validate customer (Sim43)", task: "Validate customer", "id":"74eac729-5a8a-4fbb-a19c-f4a06f2cba13", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Under 21 (Sim43)", task: "Under 21", "id":"ff356134-d3f2-43ae-87ae-162c7775370d", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Approve customer (Sim44)", task: "Approve customer", "id":"40d2a6dd-a1b7-496f-816d-3674ecb18891", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Validate customer (Sim44)", task: "Validate customer", "id":"03aca844-f0a3-499f-b865-81c36ad9f6ae", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Over 21 (Sim44)", task: "Over 21", "id":"59085409-8143-4680-a85f-2ab6e12f48ae", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Approve customer (Sim45)", task: "Approve customer", "id":"f50021af-405b-4e42-a07f-7a8bd51f527a", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Validate customer (Sim45)", task: "Validate customer", "id":"855285cd-4d4e-4b8a-8a37-72a7d9c66e55", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Over 21 (Sim45)", task: "Over 21", "id":"71f7aca1-0092-49e5-96cc-3540a2d7adff", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Approve customer (Sim46)", task: "Approve customer", "id":"2ee2afd0-8b99-429f-a54d-17ee3aece6e7", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Validate customer (Sim46)", task: "Validate customer", "id":"52d17117-d628-49c8-8e33-fc09c0194142", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Under 21 (Sim46)", task: "Under 21", "id":"0db795f1-493f-412f-934c-4959a2959846", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Approve customer (Sim47)", task: "Approve customer", "id":"5733638d-49ab-4851-9f60-3e409fe4903d", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Validate customer (Sim47)", task: "Validate customer", "id":"3c6a4a82-e751-4fa5-81ed-23355f15f0e7", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Under 21 (Sim47)", task: "Under 21", "id":"e9842d39-7c82-453f-b8aa-1a6339720c12", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Approve customer (Sim48)", task: "Approve customer", "id":"5d8dd67e-d1e0-4d57-b536-f3fe550ec4cd", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Validate customer (Sim48)", task: "Validate customer", "id":"0057fe20-d9ac-4d24-b420-54163ceeba1f", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Over 21 (Sim48)", task: "Over 21", "id":"72d06646-427a-4322-8d01-12083fc4d9bd", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Approve customer (Sim49)", task: "Approve customer", "id":"1ee8f938-7bb2-40b2-ab98-01815a41963b", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Validate customer (Sim49)", task: "Validate customer", "id":"c1596eb4-79a0-4c83-bc7d-c8b6f2708ad1", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Over 21 (Sim49)", task: "Over 21", "id":"f9d6976a-f81b-4a71-9526-39083b813230", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Approve customer (Sim5)", task: "Approve customer", "id":"d18f5132-8065-4297-805d-6b1c9dd43025", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Validate customer (Sim5)", task: "Validate customer", "id":"94a92160-433f-4b36-b95c-42b50be3e9eb", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Over 21 (Sim5)", task: "Over 21", "id":"334c5bd0-277b-42bf-86c8-eecbeb40c9fd", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Approve customer (Sim50)", task: "Approve customer", "id":"54261afe-ec9d-4bbb-abbe-f30060adc222", "starting_time": 2000, "ending_time": 2005, delay: 0, cost: 0},
	{"label":"Validate customer (Sim50)", task: "Validate customer", "id":"c8b8f345-a86b-4c33-95cd-5dc8f7dff750", "starting_time": 2005, "ending_time": 2010, delay: 0, cost: 0},
	{"label":"Under 21 (Sim50)", task: "Under 21", "id":"d9903b3b-4531-449c-8ebd-5467c3c2b047", "starting_time": 2010, "ending_time": 2015, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Approve customer (Sim51)", task: "Approve customer", "id":"50a7927f-39eb-44ea-acc7-03bb6e447a87", "starting_time": 2040, "ending_time": 2045, delay: 0, cost: 0},
	{"label":"Validate customer (Sim51)", task: "Validate customer", "id":"332ee333-2407-4e69-8fd1-296ab25c00cf", "starting_time": 2045, "ending_time": 2050, delay: 0, cost: 0},
	{"label":"Under 21 (Sim51)", task: "Under 21", "id":"9b8f237e-ea7a-483f-9dcd-65aea070b5dc", "starting_time": 2050, "ending_time": 2055, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Approve customer (Sim52)", task: "Approve customer", "id":"860eb488-450a-4467-b282-a72716a3f0ac", "starting_time": 2080, "ending_time": 2085, delay: 0, cost: 0},
	{"label":"Validate customer (Sim52)", task: "Validate customer", "id":"a4d7adfe-7251-496c-9d1e-2e321a21aea6", "starting_time": 2085, "ending_time": 2090, delay: 0, cost: 0},
	{"label":"Over 21 (Sim52)", task: "Over 21", "id":"ea2bc7e7-51d0-44d5-998b-18361fe90dca", "starting_time": 2090, "ending_time": 2095, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Approve customer (Sim53)", task: "Approve customer", "id":"42591d04-96b7-4a88-bd15-19e510f82acb", "starting_time": 2120, "ending_time": 2125, delay: 0, cost: 0},
	{"label":"Validate customer (Sim53)", task: "Validate customer", "id":"b3417d46-1a59-45b5-b16c-10f057d9acf5", "starting_time": 2125, "ending_time": 2130, delay: 0, cost: 0},
	{"label":"Over 21 (Sim53)", task: "Over 21", "id":"1aee5517-84b4-4a07-a9bb-fc478b684076", "starting_time": 2130, "ending_time": 2135, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Approve customer (Sim54)", task: "Approve customer", "id":"bb29a21b-39b1-4229-8e3d-0ae8f2153205", "starting_time": 2160, "ending_time": 2165, delay: 0, cost: 0},
	{"label":"Validate customer (Sim54)", task: "Validate customer", "id":"31b04482-c4e4-4b22-8889-68598b91cede", "starting_time": 2165, "ending_time": 2170, delay: 0, cost: 0},
	{"label":"Over 21 (Sim54)", task: "Over 21", "id":"18c36191-0e8c-426d-9fa7-3d3c1863bcc8", "starting_time": 2170, "ending_time": 2175, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Approve customer (Sim55)", task: "Approve customer", "id":"6b5cafb1-76ba-4c7f-aed0-f1c30351e865", "starting_time": 2200, "ending_time": 2205, delay: 0, cost: 0},
	{"label":"Validate customer (Sim55)", task: "Validate customer", "id":"c0401145-5e7c-4dc7-8d2b-b63163a91040", "starting_time": 2205, "ending_time": 2210, delay: 0, cost: 0},
	{"label":"Over 21 (Sim55)", task: "Over 21", "id":"d30598f4-280c-46bf-b74c-e8cf5985e32b", "starting_time": 2210, "ending_time": 2215, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Approve customer (Sim56)", task: "Approve customer", "id":"6ea76302-3a57-42fa-85de-2912ab3d0703", "starting_time": 2240, "ending_time": 2245, delay: 0, cost: 0},
	{"label":"Validate customer (Sim56)", task: "Validate customer", "id":"ae6a9105-4b1f-4f83-946d-315e3c4a05bb", "starting_time": 2245, "ending_time": 2250, delay: 0, cost: 0},
	{"label":"Over 21 (Sim56)", task: "Over 21", "id":"5d79237b-12ca-4367-ad65-e5eaf08f8e6a", "starting_time": 2250, "ending_time": 2255, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Approve customer (Sim57)", task: "Approve customer", "id":"61c1cc87-1617-496e-b91a-8b90811c85d0", "starting_time": 2280, "ending_time": 2285, delay: 0, cost: 0},
	{"label":"Validate customer (Sim57)", task: "Validate customer", "id":"81b14325-648b-4f39-bba5-0f7f0127ef46", "starting_time": 2285, "ending_time": 2290, delay: 0, cost: 0},
	{"label":"Under 21 (Sim57)", task: "Under 21", "id":"af607f79-300b-4eb5-b059-42ee17dc9acc", "starting_time": 2290, "ending_time": 2295, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Approve customer (Sim58)", task: "Approve customer", "id":"0431ca81-7ad5-4832-aa6b-2e32dd9ee444", "starting_time": 2320, "ending_time": 2325, delay: 0, cost: 0},
	{"label":"Validate customer (Sim58)", task: "Validate customer", "id":"282c95ec-b425-4a69-a163-a738b40993a5", "starting_time": 2325, "ending_time": 2330, delay: 0, cost: 0},
	{"label":"Over 21 (Sim58)", task: "Over 21", "id":"5975d67f-daeb-4b1e-815f-40e2b80891ab", "starting_time": 2330, "ending_time": 2335, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Approve customer (Sim59)", task: "Approve customer", "id":"96b16680-7317-4094-bec5-bf43fe37fcd0", "starting_time": 2360, "ending_time": 2365, delay: 0, cost: 0},
	{"label":"Validate customer (Sim59)", task: "Validate customer", "id":"f1a6284a-bd97-430b-8cdb-b8d631df56ca", "starting_time": 2365, "ending_time": 2370, delay: 0, cost: 0},
	{"label":"Over 21 (Sim59)", task: "Over 21", "id":"dde9d5f1-e138-41f3-945a-257a4e322c44", "starting_time": 2370, "ending_time": 2375, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Approve customer (Sim6)", task: "Approve customer", "id":"e5cfb5da-7f4d-4c57-b6f1-f2c5a63fa3db", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Validate customer (Sim6)", task: "Validate customer", "id":"54cfead8-54fa-4d00-bfa1-371cd4487216", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Over 21 (Sim6)", task: "Over 21", "id":"b8f2946a-ff50-4869-9030-5dcaf0f9c9a9", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Approve customer (Sim60)", task: "Approve customer", "id":"abe3ff8a-9690-43a8-8548-9519eea70163", "starting_time": 2400, "ending_time": 2405, delay: 0, cost: 0},
	{"label":"Validate customer (Sim60)", task: "Validate customer", "id":"9b318ac8-e82b-4b20-9db2-a5c080c8428c", "starting_time": 2405, "ending_time": 2410, delay: 0, cost: 0},
	{"label":"Over 21 (Sim60)", task: "Over 21", "id":"6df428db-6a44-4607-a002-96d73267f7fd", "starting_time": 2410, "ending_time": 2415, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Approve customer (Sim61)", task: "Approve customer", "id":"4a0812e8-e249-411f-848f-3963bf1db301", "starting_time": 2440, "ending_time": 2445, delay: 0, cost: 0},
	{"label":"Validate customer (Sim61)", task: "Validate customer", "id":"be08048e-f1a4-432c-9326-910486c7d36b", "starting_time": 2445, "ending_time": 2450, delay: 0, cost: 0},
	{"label":"Under 21 (Sim61)", task: "Under 21", "id":"0d9e3574-fe06-4941-9738-fef232ffeb00", "starting_time": 2450, "ending_time": 2455, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Approve customer (Sim62)", task: "Approve customer", "id":"f689ad94-1d06-4e3e-9d85-ce263406ea66", "starting_time": 2480, "ending_time": 2485, delay: 0, cost: 0},
	{"label":"Validate customer (Sim62)", task: "Validate customer", "id":"7ea077e5-29e7-4975-8ee4-5685a7d19f15", "starting_time": 2485, "ending_time": 2490, delay: 0, cost: 0},
	{"label":"Under 21 (Sim62)", task: "Under 21", "id":"57a780d1-55bc-41f1-8a75-ec07972b6cb5", "starting_time": 2490, "ending_time": 2495, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Approve customer (Sim63)", task: "Approve customer", "id":"966fea57-ee01-4ff3-aacb-63e551a3b307", "starting_time": 2520, "ending_time": 2525, delay: 0, cost: 0},
	{"label":"Validate customer (Sim63)", task: "Validate customer", "id":"50fc0c0b-fbeb-470d-b251-35dbcd594167", "starting_time": 2525, "ending_time": 2530, delay: 0, cost: 0},
	{"label":"Under 21 (Sim63)", task: "Under 21", "id":"53c951fb-c41e-4309-9259-6af13ad3dd53", "starting_time": 2530, "ending_time": 2535, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Approve customer (Sim64)", task: "Approve customer", "id":"46cd295e-e979-46ed-b813-71f47fb98884", "starting_time": 2560, "ending_time": 2565, delay: 0, cost: 0},
	{"label":"Validate customer (Sim64)", task: "Validate customer", "id":"a756ee3e-4dc9-4472-b229-69db13c70595", "starting_time": 2565, "ending_time": 2570, delay: 0, cost: 0},
	{"label":"Over 21 (Sim64)", task: "Over 21", "id":"f5c7f026-c50c-4506-9037-6776e53e535b", "starting_time": 2570, "ending_time": 2575, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Approve customer (Sim65)", task: "Approve customer", "id":"5cd67dff-dbda-4d11-8579-5ba79f0b28a8", "starting_time": 2600, "ending_time": 2605, delay: 0, cost: 0},
	{"label":"Validate customer (Sim65)", task: "Validate customer", "id":"f1c93cf5-17ba-402f-8636-fd1007a4f404", "starting_time": 2605, "ending_time": 2610, delay: 0, cost: 0},
	{"label":"Over 21 (Sim65)", task: "Over 21", "id":"1ab06e39-7d5b-4b09-8116-0bce124f2994", "starting_time": 2610, "ending_time": 2615, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Approve customer (Sim66)", task: "Approve customer", "id":"cc9548c4-02e5-4461-9925-ce191243e284", "starting_time": 2640, "ending_time": 2645, delay: 0, cost: 0},
	{"label":"Validate customer (Sim66)", task: "Validate customer", "id":"276a2079-2285-481f-af36-3a91f7ef9c90", "starting_time": 2645, "ending_time": 2650, delay: 0, cost: 0},
	{"label":"Under 21 (Sim66)", task: "Under 21", "id":"050a8cac-ab36-419e-8a5a-a3cd29b67a77", "starting_time": 2650, "ending_time": 2655, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Approve customer (Sim67)", task: "Approve customer", "id":"58df7f33-7d5d-4dd2-b2f6-12af9102ce80", "starting_time": 2680, "ending_time": 2685, delay: 0, cost: 0},
	{"label":"Validate customer (Sim67)", task: "Validate customer", "id":"0259966c-1c51-4b85-ab1e-e9245b14917d", "starting_time": 2685, "ending_time": 2690, delay: 0, cost: 0},
	{"label":"Under 21 (Sim67)", task: "Under 21", "id":"3f7fcf7b-b18e-451b-b9f6-764b660a9cce", "starting_time": 2690, "ending_time": 2695, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Approve customer (Sim68)", task: "Approve customer", "id":"fb07e38c-e051-4fa7-98ec-a420a65c2968", "starting_time": 2720, "ending_time": 2725, delay: 0, cost: 0},
	{"label":"Validate customer (Sim68)", task: "Validate customer", "id":"cd3d12cd-45a2-4526-9a4e-1b7cbf8092dc", "starting_time": 2725, "ending_time": 2730, delay: 0, cost: 0},
	{"label":"Over 21 (Sim68)", task: "Over 21", "id":"4323adf3-f3da-4fb2-903d-7095767fb2e9", "starting_time": 2730, "ending_time": 2735, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Approve customer (Sim69)", task: "Approve customer", "id":"5a2ffb3f-8dd8-49f7-85cc-cd8fca88ff1b", "starting_time": 2760, "ending_time": 2765, delay: 0, cost: 0},
	{"label":"Validate customer (Sim69)", task: "Validate customer", "id":"c1a608d3-511e-422f-96ed-315a8b649394", "starting_time": 2765, "ending_time": 2770, delay: 0, cost: 0},
	{"label":"Under 21 (Sim69)", task: "Under 21", "id":"ec5f34a9-e2f4-4117-add5-9a122d40b903", "starting_time": 2770, "ending_time": 2775, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Approve customer (Sim7)", task: "Approve customer", "id":"d32b031c-c695-43c9-b56a-986047c67b89", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Validate customer (Sim7)", task: "Validate customer", "id":"f661e070-3b86-4a24-ada6-f8297d335c61", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Under 21 (Sim7)", task: "Under 21", "id":"0afc692a-73e2-4559-8174-1d7ada7c7e18", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Approve customer (Sim70)", task: "Approve customer", "id":"4763719f-71f1-4929-8429-3bfabeb5a2dd", "starting_time": 2800, "ending_time": 2805, delay: 0, cost: 0},
	{"label":"Validate customer (Sim70)", task: "Validate customer", "id":"75f1ab15-cac0-44de-a286-2a6df8832de3", "starting_time": 2805, "ending_time": 2810, delay: 0, cost: 0},
	{"label":"Under 21 (Sim70)", task: "Under 21", "id":"b865f2d4-c4f5-48a6-a4b2-fb25c3e6e0b8", "starting_time": 2810, "ending_time": 2815, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Approve customer (Sim71)", task: "Approve customer", "id":"ddbb7f08-8ae7-45d2-9ecd-5dba5fabb991", "starting_time": 2840, "ending_time": 2845, delay: 0, cost: 0},
	{"label":"Validate customer (Sim71)", task: "Validate customer", "id":"52fce777-4308-4114-8aaf-def9c9425128", "starting_time": 2845, "ending_time": 2850, delay: 0, cost: 0},
	{"label":"Over 21 (Sim71)", task: "Over 21", "id":"aecf5934-68b2-4247-b8d4-72d661575f2b", "starting_time": 2850, "ending_time": 2855, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Approve customer (Sim72)", task: "Approve customer", "id":"ff51d378-754e-4fbd-8a98-1a09db7f6b05", "starting_time": 2880, "ending_time": 2885, delay: 0, cost: 0},
	{"label":"Validate customer (Sim72)", task: "Validate customer", "id":"0b7f8f25-e4cf-4a9a-8daa-1272444e433f", "starting_time": 2885, "ending_time": 2890, delay: 0, cost: 0},
	{"label":"Under 21 (Sim72)", task: "Under 21", "id":"2cf197c5-3207-4b64-9d65-7ff2629638a4", "starting_time": 2890, "ending_time": 2895, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Approve customer (Sim73)", task: "Approve customer", "id":"8aa9e55d-d797-4c7d-abc3-18ad9879d34b", "starting_time": 2920, "ending_time": 2925, delay: 0, cost: 0},
	{"label":"Validate customer (Sim73)", task: "Validate customer", "id":"94b536a3-74ea-4d87-8304-c42563b3dc3b", "starting_time": 2925, "ending_time": 2930, delay: 0, cost: 0},
	{"label":"Over 21 (Sim73)", task: "Over 21", "id":"9e7bc6a9-44b6-4527-a833-6e0cc3c817fd", "starting_time": 2930, "ending_time": 2935, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Approve customer (Sim74)", task: "Approve customer", "id":"241eca03-01d4-4194-b2c4-b4b23d6198f5", "starting_time": 2960, "ending_time": 2965, delay: 0, cost: 0},
	{"label":"Validate customer (Sim74)", task: "Validate customer", "id":"1fd084af-92e2-4e74-897a-9753b245c4b5", "starting_time": 2965, "ending_time": 2970, delay: 0, cost: 0},
	{"label":"Over 21 (Sim74)", task: "Over 21", "id":"c715ee46-d3e7-4086-8a8d-7e4d74208ada", "starting_time": 2970, "ending_time": 2975, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Approve customer (Sim75)", task: "Approve customer", "id":"781be68a-3ee4-4bc1-9076-876c25698582", "starting_time": 3000, "ending_time": 3005, delay: 0, cost: 0},
	{"label":"Validate customer (Sim75)", task: "Validate customer", "id":"93cf7b81-ee18-4dda-a4a3-9055d1af208b", "starting_time": 3005, "ending_time": 3010, delay: 0, cost: 0},
	{"label":"Over 21 (Sim75)", task: "Over 21", "id":"6cd04dfd-9c8b-4088-9013-d80f38555907", "starting_time": 3010, "ending_time": 3015, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Approve customer (Sim76)", task: "Approve customer", "id":"fa4a0e89-14b0-4def-b0bf-373b1bebda4c", "starting_time": 3040, "ending_time": 3045, delay: 0, cost: 0},
	{"label":"Validate customer (Sim76)", task: "Validate customer", "id":"31eac421-24a8-4cc7-9cc8-c264eb27f3ea", "starting_time": 3045, "ending_time": 3050, delay: 0, cost: 0},
	{"label":"Over 21 (Sim76)", task: "Over 21", "id":"6486c19b-0d95-4824-8cbe-f5b079bf3a8c", "starting_time": 3050, "ending_time": 3055, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Approve customer (Sim77)", task: "Approve customer", "id":"3e1394f9-a5c0-4d1f-93f7-52e65346fcef", "starting_time": 3080, "ending_time": 3085, delay: 0, cost: 0},
	{"label":"Validate customer (Sim77)", task: "Validate customer", "id":"d46d1dc0-fdc0-4849-84c4-5399d4e1cb13", "starting_time": 3085, "ending_time": 3090, delay: 0, cost: 0},
	{"label":"Over 21 (Sim77)", task: "Over 21", "id":"941a7e02-4879-4312-aeff-33b20ae2e172", "starting_time": 3090, "ending_time": 3095, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Approve customer (Sim78)", task: "Approve customer", "id":"e1f027d3-e58a-4250-919b-9c276664a47f", "starting_time": 3120, "ending_time": 3125, delay: 0, cost: 0},
	{"label":"Validate customer (Sim78)", task: "Validate customer", "id":"dcbc3f93-fccc-4d94-b3f7-4ea076b15daa", "starting_time": 3125, "ending_time": 3130, delay: 0, cost: 0},
	{"label":"Over 21 (Sim78)", task: "Over 21", "id":"f440bae1-f1ca-4e5f-861a-c64fd510b147", "starting_time": 3130, "ending_time": 3135, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Approve customer (Sim79)", task: "Approve customer", "id":"f413998a-5a79-42e4-ab86-953f5312cc00", "starting_time": 3160, "ending_time": 3165, delay: 0, cost: 0},
	{"label":"Validate customer (Sim79)", task: "Validate customer", "id":"514ad57b-8202-4452-82b6-c28753673c60", "starting_time": 3165, "ending_time": 3170, delay: 0, cost: 0},
	{"label":"Over 21 (Sim79)", task: "Over 21", "id":"739051fc-8949-413f-9d4e-8a06f09cd307", "starting_time": 3170, "ending_time": 3175, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Approve customer (Sim8)", task: "Approve customer", "id":"5a3706f3-0136-4938-843f-035e8fb232e2", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Validate customer (Sim8)", task: "Validate customer", "id":"b5e585b3-a560-4cab-b0d8-1c3332071b4e", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Under 21 (Sim8)", task: "Under 21", "id":"0987d54b-780d-4e96-90f5-591af38cc7cc", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Approve customer (Sim80)", task: "Approve customer", "id":"3a37d023-13c3-42de-a205-92d982b238fd", "starting_time": 3200, "ending_time": 3205, delay: 0, cost: 0},
	{"label":"Validate customer (Sim80)", task: "Validate customer", "id":"8aeb86c5-cee5-41a8-97e9-7a05a2fa0538", "starting_time": 3205, "ending_time": 3210, delay: 0, cost: 0},
	{"label":"Over 21 (Sim80)", task: "Over 21", "id":"6eab2c5a-ba3a-4df1-922a-f02c811f839f", "starting_time": 3210, "ending_time": 3215, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Approve customer (Sim81)", task: "Approve customer", "id":"c89ed0f2-b428-4097-90f3-9980b75a1d1b", "starting_time": 3240, "ending_time": 3245, delay: 0, cost: 0},
	{"label":"Validate customer (Sim81)", task: "Validate customer", "id":"a957cb38-a14b-4be4-a0df-04ce1437f3bb", "starting_time": 3245, "ending_time": 3250, delay: 0, cost: 0},
	{"label":"Over 21 (Sim81)", task: "Over 21", "id":"9f6d9ab2-bc4b-449f-be1f-395f4433128b", "starting_time": 3250, "ending_time": 3255, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Approve customer (Sim82)", task: "Approve customer", "id":"17df2c0d-bc66-4450-918b-a833b3487479", "starting_time": 3280, "ending_time": 3285, delay: 0, cost: 0},
	{"label":"Validate customer (Sim82)", task: "Validate customer", "id":"7f76e973-be1b-4597-8fc2-548bf5059a33", "starting_time": 3285, "ending_time": 3290, delay: 0, cost: 0},
	{"label":"Under 21 (Sim82)", task: "Under 21", "id":"7c69e10a-fbee-4874-a725-5ff9d4becce2", "starting_time": 3290, "ending_time": 3295, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Approve customer (Sim83)", task: "Approve customer", "id":"70cc09e5-f8c1-4bb6-95dc-f6ded78e6c78", "starting_time": 3320, "ending_time": 3325, delay: 0, cost: 0},
	{"label":"Validate customer (Sim83)", task: "Validate customer", "id":"6bc23f83-7d61-4501-8a2d-5c7c5c6871f0", "starting_time": 3325, "ending_time": 3330, delay: 0, cost: 0},
	{"label":"Under 21 (Sim83)", task: "Under 21", "id":"a95b9203-f68b-453b-980c-301392a04678", "starting_time": 3330, "ending_time": 3335, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Approve customer (Sim84)", task: "Approve customer", "id":"7ceca141-d4a0-4867-971b-cd953365ad34", "starting_time": 3360, "ending_time": 3365, delay: 0, cost: 0},
	{"label":"Validate customer (Sim84)", task: "Validate customer", "id":"f6ff058c-8745-4233-ad69-a5451ef984b6", "starting_time": 3365, "ending_time": 3370, delay: 0, cost: 0},
	{"label":"Under 21 (Sim84)", task: "Under 21", "id":"ecc9f158-510f-420c-8665-ef567f04127e", "starting_time": 3370, "ending_time": 3375, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Approve customer (Sim85)", task: "Approve customer", "id":"6f20d839-00b1-426a-9336-3fb71292e3b1", "starting_time": 3400, "ending_time": 3405, delay: 0, cost: 0},
	{"label":"Validate customer (Sim85)", task: "Validate customer", "id":"0cecf768-f591-452e-ab9f-1cde546f6fc7", "starting_time": 3405, "ending_time": 3410, delay: 0, cost: 0},
	{"label":"Over 21 (Sim85)", task: "Over 21", "id":"11ac4fa7-8b1b-46bf-8304-e2876d8276e5", "starting_time": 3410, "ending_time": 3415, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Approve customer (Sim86)", task: "Approve customer", "id":"d8787ad4-4d47-422b-a46c-c564cd9d8017", "starting_time": 3440, "ending_time": 3445, delay: 0, cost: 0},
	{"label":"Validate customer (Sim86)", task: "Validate customer", "id":"757b2f4f-d1d4-455c-a168-1917bc93a034", "starting_time": 3445, "ending_time": 3450, delay: 0, cost: 0},
	{"label":"Under 21 (Sim86)", task: "Under 21", "id":"d90f45cd-b27a-4548-89d2-7eee85cc9ede", "starting_time": 3450, "ending_time": 3455, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Approve customer (Sim87)", task: "Approve customer", "id":"674009c3-8a69-4c6f-8923-8c5e54ac1aad", "starting_time": 3480, "ending_time": 3485, delay: 0, cost: 0},
	{"label":"Validate customer (Sim87)", task: "Validate customer", "id":"98da80d6-9105-4ed7-a003-e19eba1043d1", "starting_time": 3485, "ending_time": 3490, delay: 0, cost: 0},
	{"label":"Over 21 (Sim87)", task: "Over 21", "id":"12e2e93c-4dd1-4849-bd7c-848d45cb2b90", "starting_time": 3490, "ending_time": 3495, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Approve customer (Sim88)", task: "Approve customer", "id":"177f318c-6ed7-4e43-acf7-f4956d6e14fe", "starting_time": 3520, "ending_time": 3525, delay: 0, cost: 0},
	{"label":"Validate customer (Sim88)", task: "Validate customer", "id":"c19188e7-8da9-44b7-8a3d-5aba8766f26c", "starting_time": 3525, "ending_time": 3530, delay: 0, cost: 0},
	{"label":"Under 21 (Sim88)", task: "Under 21", "id":"d050e999-aef6-43b1-9c5d-c652dc8f2ff2", "starting_time": 3530, "ending_time": 3535, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Approve customer (Sim89)", task: "Approve customer", "id":"d292d71a-e27d-425a-8fb3-60a50d51769f", "starting_time": 3560, "ending_time": 3565, delay: 0, cost: 0},
	{"label":"Validate customer (Sim89)", task: "Validate customer", "id":"638b30ea-2050-469c-b790-fd70b9eecebc", "starting_time": 3565, "ending_time": 3570, delay: 0, cost: 0},
	{"label":"Over 21 (Sim89)", task: "Over 21", "id":"8610eb3c-fc58-4e22-a3dd-15fa516156b7", "starting_time": 3570, "ending_time": 3575, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Approve customer (Sim9)", task: "Approve customer", "id":"cf03b04c-4652-45e4-989f-b8557710404f", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Validate customer (Sim9)", task: "Validate customer", "id":"3017e6b7-ea16-43e5-ab59-395353db8996", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Under 21 (Sim9)", task: "Under 21", "id":"9184df7d-714c-489c-9b0b-ed81e70d7a95", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Approve customer (Sim90)", task: "Approve customer", "id":"b64ace3f-688a-43a4-a7dc-811d79ff165d", "starting_time": 3600, "ending_time": 3605, delay: 0, cost: 0},
	{"label":"Validate customer (Sim90)", task: "Validate customer", "id":"e8dc924a-d1bb-46a7-8c7a-24719da048b3", "starting_time": 3605, "ending_time": 3610, delay: 0, cost: 0},
	{"label":"Over 21 (Sim90)", task: "Over 21", "id":"fbec4bcc-29bc-4435-b691-838197e6663f", "starting_time": 3610, "ending_time": 3615, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Approve customer (Sim91)", task: "Approve customer", "id":"722c8371-4cb7-4604-8ad2-36f5239c8279", "starting_time": 3640, "ending_time": 3645, delay: 0, cost: 0},
	{"label":"Validate customer (Sim91)", task: "Validate customer", "id":"3cbed014-585a-4b06-bd17-940f7acc0c13", "starting_time": 3645, "ending_time": 3650, delay: 0, cost: 0},
	{"label":"Under 21 (Sim91)", task: "Under 21", "id":"e11bfc86-7371-4e97-828e-88438182d7b0", "starting_time": 3650, "ending_time": 3655, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Approve customer (Sim92)", task: "Approve customer", "id":"9f5e31cd-746c-47d3-acfb-867ee6f40dfa", "starting_time": 3680, "ending_time": 3685, delay: 0, cost: 0},
	{"label":"Validate customer (Sim92)", task: "Validate customer", "id":"6126d21b-fe1c-4627-9c19-2ef5d529ff4d", "starting_time": 3685, "ending_time": 3690, delay: 0, cost: 0},
	{"label":"Under 21 (Sim92)", task: "Under 21", "id":"ef7568fe-3f3f-48ae-b869-2ef15485f072", "starting_time": 3690, "ending_time": 3695, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Approve customer (Sim93)", task: "Approve customer", "id":"167efc49-b5ee-47a7-9836-f6e1f065841c", "starting_time": 3720, "ending_time": 3725, delay: 0, cost: 0},
	{"label":"Validate customer (Sim93)", task: "Validate customer", "id":"705c2e72-939e-4722-b47d-22ce954b025f", "starting_time": 3725, "ending_time": 3730, delay: 0, cost: 0},
	{"label":"Under 21 (Sim93)", task: "Under 21", "id":"9fbbe289-f5e1-40a3-a214-911364a0cecb", "starting_time": 3730, "ending_time": 3735, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Approve customer (Sim94)", task: "Approve customer", "id":"5d4ae366-9a34-4c4c-8d47-d825ea321e3f", "starting_time": 3760, "ending_time": 3765, delay: 0, cost: 0},
	{"label":"Validate customer (Sim94)", task: "Validate customer", "id":"d8c36607-260b-4099-9d4a-55b9b6bde33d", "starting_time": 3765, "ending_time": 3770, delay: 0, cost: 0},
	{"label":"Over 21 (Sim94)", task: "Over 21", "id":"4ddf42e3-63cd-4abe-8ac0-8165fc1e9bba", "starting_time": 3770, "ending_time": 3775, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Approve customer (Sim95)", task: "Approve customer", "id":"f7bf53d9-c6c2-4b06-8ec9-e74559e509b1", "starting_time": 3800, "ending_time": 3805, delay: 0, cost: 0},
	{"label":"Validate customer (Sim95)", task: "Validate customer", "id":"41ea79d5-bd83-488c-b2ee-e8c634c18e6e", "starting_time": 3805, "ending_time": 3810, delay: 0, cost: 0},
	{"label":"Over 21 (Sim95)", task: "Over 21", "id":"75c6d820-6187-4807-a770-ee48228fb063", "starting_time": 3810, "ending_time": 3815, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Approve customer (Sim96)", task: "Approve customer", "id":"6569bb78-5ab6-4460-b6c6-cc677c2becd2", "starting_time": 3840, "ending_time": 3845, delay: 0, cost: 0},
	{"label":"Validate customer (Sim96)", task: "Validate customer", "id":"cb29e592-3ba9-4746-9612-96916e8738be", "starting_time": 3845, "ending_time": 3850, delay: 0, cost: 0},
	{"label":"Under 21 (Sim96)", task: "Under 21", "id":"730a193a-5922-4a56-9c85-8b10b625d8ea", "starting_time": 3850, "ending_time": 3855, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Approve customer (Sim97)", task: "Approve customer", "id":"65f38376-6232-402b-a6e3-7c18b67cce13", "starting_time": 3880, "ending_time": 3885, delay: 0, cost: 0},
	{"label":"Validate customer (Sim97)", task: "Validate customer", "id":"11670644-29c9-4d51-820d-07a788ce443c", "starting_time": 3885, "ending_time": 3890, delay: 0, cost: 0},
	{"label":"Under 21 (Sim97)", task: "Under 21", "id":"17d54e67-da76-473b-bb93-bf21489d33af", "starting_time": 3890, "ending_time": 3895, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Approve customer (Sim98)", task: "Approve customer", "id":"46654b25-67ec-4e5f-8b02-5620220a0504", "starting_time": 3920, "ending_time": 3925, delay: 0, cost: 0},
	{"label":"Validate customer (Sim98)", task: "Validate customer", "id":"29b257bb-7ae1-463f-8cc7-f71aa751dfc1", "starting_time": 3925, "ending_time": 3930, delay: 0, cost: 0},
	{"label":"Over 21 (Sim98)", task: "Over 21", "id":"644aebb6-431c-4c91-8440-6960820acb9f", "starting_time": 3930, "ending_time": 3935, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Approve customer (Sim99)", task: "Approve customer", "id":"5f9dea3f-75e6-43fc-b021-509dea4712a1", "starting_time": 3960, "ending_time": 3965, delay: 0, cost: 0},
	{"label":"Validate customer (Sim99)", task: "Validate customer", "id":"8419fe8b-59ec-4658-84d1-f507b8731642", "starting_time": 3965, "ending_time": 3970, delay: 0, cost: 0},
	{"label":"Over 21 (Sim99)", task: "Over 21", "id":"2ba2a2ad-8b22-492b-a033-4f692a173f03", "starting_time": 3970, "ending_time": 3975, delay: 0, cost: 0}
]},
];
