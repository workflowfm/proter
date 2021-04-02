var tasks = [
	"Script Task",
	"Service Task",
	"User Task A",
	"User Task B",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"aaf633ae-a3ee-4767-9f1e-6aed04c1e19f", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"1d400097-8215-4eb2-95a1-2425f8d17a07", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"907b5c15-b744-4f98-bd4d-e469ea169f4b", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0},
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"471d9026-0584-404e-a7f3-fcb599874d8e", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"60902114-830e-40b6-9ec1-867157b30366", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"2682a9b9-e4a3-4422-9925-407228848386", "starting_time": 30, "ending_time": 35, delay: 0, cost: 0},
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"09188d2b-1c8f-467b-a413-1f0c6f11ee11", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"81d45e39-9289-469b-be85-86fed258653e", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"59aa0704-adf9-4788-82a3-578c6ab2472b", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0},
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"d091c867-6c78-49b3-b31d-ff8b6b020d13", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"8534b60f-59d6-43cd-ab85-88c7555c3e33", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"86c04265-04a6-4acb-8c57-0c53d40d9a7b", "starting_time": 70, "ending_time": 75, delay: 0, cost: 0},
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"c4100fd7-450f-4fe2-942b-340ba6c118fd", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"be41c7ce-343c-40c6-99be-72cfb35f9f13", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"7a717544-42a6-41c6-8879-a88f7a449d47", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0},
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"2310bc50-4b64-425d-a602-0f5434e87d61", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"54f227fb-0eed-445f-b51d-488a15e8ed1c", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"14f53b5e-dd5f-4523-845a-86f03ceed984", "starting_time": 110, "ending_time": 115, delay: 0, cost: 0},
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"361b0205-fffa-4f33-aa8d-8cc0c9df2c69", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"204ca0e1-72f9-4ba9-9683-31d2f1544e4e", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"59d3c4e7-3643-4c88-a91f-61aacc8d150e", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0},
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"f97eeb53-c408-47da-8979-2214ad3b07ae", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"2987e347-0f51-4a89-aa4f-13bfe6169f67", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"8b79c801-6880-4c38-84c0-506711467d8e", "starting_time": 150, "ending_time": 155, delay: 0, cost: 0},
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"a5da92d8-99d7-4c03-a15d-2c1a17b757e0", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"d03a3830-5267-4c83-8eeb-f47cf1b82790", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"413570f6-a091-4e2c-a78f-5024b35d92f4", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0},
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"5c8c9246-4f94-4245-901f-14c84a0abfb7", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"59935fdd-2554-4762-8156-e63e798179aa", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"cbc86395-95b1-462f-813a-5316c6746fdf", "starting_time": 190, "ending_time": 195, delay: 0, cost: 0},
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"5e31f4d7-aae8-46dd-865d-458a9fac38c4", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"caa39e5f-2f9f-43c6-9367-3d000a803421", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"d4681fee-9f56-42d0-b1a4-041c4fee3eea", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0},
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"4d1f6bfc-974d-4e58-80f8-570aae296d86", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"f335fb4e-47a4-4f08-9656-3f04df8e4d17", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"4f8bbd5e-1564-4252-97c1-f8e799c2727c", "starting_time": 230, "ending_time": 235, delay: 0, cost: 0},
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"4189a5cd-8f2e-4fe4-84d3-51d41e981313", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"dc019a07-2f35-4437-ad5e-0c9a53e9a4dd", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"74a2a874-d32e-432e-877e-fb5c74d91ab2", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0},
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"3927f0bd-9fa5-4595-894f-6530b22c2c17", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"2eadbd86-e789-4add-96d7-9a499626f180", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"31ddbf23-0219-4523-9527-c6b0d3e2a762", "starting_time": 270, "ending_time": 275, delay: 0, cost: 0},
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"7f204178-f9f2-484f-be8e-2b1f0d30e7b3", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"bcf54941-f926-46c3-8439-170b8587878c", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"69135402-4920-4322-b7be-bb3f630e29fb", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0},
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"cbbad666-0263-4a8f-ac75-114687009e6e", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"76fcd719-c421-4368-ba8b-200e0105be6f", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"4e6c3ce5-ae15-46a4-940f-d6c5b1c67b00", "starting_time": 310, "ending_time": 315, delay: 0, cost: 0},
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"6bc4717d-0597-433f-94ab-4839b4dfd87e", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"56994018-5521-493d-9258-0635579b491b", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"15e7294d-473b-4b00-b816-626fe724e98d", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0},
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"aa76dbcc-d78a-4ec1-9723-002168bf93b5", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"5bc5dd4c-0e0c-44e3-843b-76f7567668e0", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"ccad4f7d-0158-475c-837e-3a291e8bd53c", "starting_time": 350, "ending_time": 355, delay: 0, cost: 0},
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"0077c60f-c58c-43ab-84ed-c7609f33b426", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"e5f234bb-8197-4717-8e5d-19b1a967711c", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"0280d7c5-582e-47e7-8d53-2fc100f73bad", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0},
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"620b4e53-c99b-474b-8f6e-572546968ea4", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"3994dcd9-4c7b-4545-b0ec-946b7f252a42", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"21e803f6-cd73-4cc1-b663-48b057ee0203", "starting_time": 390, "ending_time": 395, delay: 0, cost: 0},
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"e497dc64-7c32-46b7-ad2a-c3f242bcd967", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"01f97562-67f0-4554-b795-b07dc13421a8", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"7bf6721b-bdaf-4756-a49b-d06413e93cbc", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0},
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"ea56ede2-1780-4891-86ec-1b277092317e", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"976de207-f07b-4ad7-8286-30e20eabb151", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"68a8d7b4-2cc3-42db-8a4f-a5e2163a1625", "starting_time": 430, "ending_time": 435, delay: 0, cost: 0},
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"13fa399d-3861-448b-a45e-24c9b2498986", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"b5ad8d20-d9d2-4f36-939a-008e86e0c64d", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"3b0a07ff-9773-4017-ae37-2d4884a4576c", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0},
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"05871da8-906a-4649-8717-cd705b1d077e", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"5d23a11d-3175-4c7e-aceb-edf00bd14326", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"f7b65c9b-08d2-452e-879c-d21e0db8e93d", "starting_time": 470, "ending_time": 475, delay: 0, cost: 0},
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"0aa143c5-4907-4d93-ae6b-fab6dd44fda3", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"53cf2ec0-62cd-4c8a-beed-a3399e1738d5", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"876e684c-b51d-4fa5-a535-55a7814014f3", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0},
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"68f195ff-4ac1-407e-ab37-56b8529466f9", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"d5449c29-0ee6-46e3-9a7d-cc5234546607", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"a306dffd-da74-46c6-97f7-c1214243a421", "starting_time": 510, "ending_time": 515, delay: 0, cost: 0},
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"2b252639-a945-4581-af6a-d6199426d36e", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"46161eff-30ea-4684-972d-0964218d6f03", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"2eb3d1a8-b08a-4a41-acbf-61a0895d33ad", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0},
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"acedd623-33f9-4f7f-9041-b745ddf45251", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"c633fd01-5b15-4fb1-84ad-55d1f7a96a94", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"54c76ee2-fab9-45e5-9e1f-da201c5bf810", "starting_time": 550, "ending_time": 555, delay: 0, cost: 0},
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"25c63157-e386-4fa4-9c7b-a6257d6e7ed0", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"dc95b11f-99a1-4853-a3cf-6b34122a102a", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"d59c3385-97ea-4a24-b8c2-e654cf064a64", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0},
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"93b86958-c6dd-47f3-a6fb-47457cf1be77", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"9c2166fc-22fd-45bb-a7d1-598e24019171", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"8884ca9f-1ac0-4208-a0ee-ca35d34eb7f0", "starting_time": 590, "ending_time": 595, delay: 0, cost: 0},
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"cfe9e5bf-61e2-401f-9c1e-92f3e83a9ade", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"a4443963-3175-408a-ba8f-e97d8b73dd02", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"b720448f-e21c-4f24-a410-51809679266b", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0},
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"6fbb7750-a660-4103-a534-255335fa6a7e", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"372dc35e-a301-4ff1-bd24-9406ac711c5f", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"3a2b3d4b-ecb7-4917-9131-93dbac2c086a", "starting_time": 630, "ending_time": 635, delay: 0, cost: 0},
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"bf0a5fef-cccf-4751-b2ed-c7bf953adefb", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"deee92ff-9ed0-41d0-b8cb-e1946bd00eba", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"8865d475-5cf5-46d9-b02e-5d9bda20cbc2", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0},
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"4624c353-6b1a-4921-9d2e-51aa8c4bb331", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"c8a3e3f1-25be-4a95-9790-496bb6467ff6", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"c61b7fbd-d2f5-48f7-a89e-829fcb99b8ff", "starting_time": 670, "ending_time": 675, delay: 0, cost: 0},
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"0ac13bad-b72d-4276-b733-8ad2085223a6", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"1dcb5352-b58b-438d-9408-8ad0c779ea52", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"48a80638-40b0-4d42-a00a-789053d36a46", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0},
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"501741ca-bf28-4896-9ab4-0cb05bceda35", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"3e81ba3f-f1a4-4167-b8f1-21f81d7b0107", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"fcc41607-7b6f-4727-b4ef-4aac444cb3a6", "starting_time": 710, "ending_time": 715, delay: 0, cost: 0},
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"323f71cf-2afd-482f-8092-f7ffe483b55c", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"e55a14e7-2eaf-47ad-bef2-3447653b3918", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"b7ee2d2f-3236-4aa6-994c-3365a949f8d2", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0},
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"496a4291-ba19-4937-8560-ff7d38a92ef0", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"f1af7429-7111-4f15-8c47-0c0d340a8f41", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"8ca81918-3680-4876-b55f-f3e18adc0d13", "starting_time": 750, "ending_time": 755, delay: 0, cost: 0},
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"a28c134e-0fcb-4ff9-946a-e406fcda9fbc", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"a908fdad-a6b0-4bf3-9cba-265c472b893a", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"18f35302-ce89-428e-b9ed-cb79545a7c74", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0},
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"76c511ee-18dd-47f8-9311-67df2813c37b", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"489948be-6102-408a-a1aa-39d95bfc88f0", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"209acbcd-80c3-4c97-a44b-df8574092fd6", "starting_time": 790, "ending_time": 795, delay: 0, cost: 0},
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"f49965ab-9d79-4a7c-ba1b-88962f88f633", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"e340569c-f2fb-4730-8200-1bbe671050c8", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"f21af81a-5228-4eb4-bd43-1c2b640beaef", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0},
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"ae757ffd-4660-4a5d-9226-837678d42037", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"5195ec5e-6953-4064-9590-755b0bbbda70", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"b2e7e749-e40d-4fd6-8887-e0304b0400b4", "starting_time": 830, "ending_time": 835, delay: 0, cost: 0},
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"d1300fe7-c26c-4e8f-aca2-57bb53fee82c", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"11d1e190-ef7b-43a9-a6b5-b3365f717135", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"9c1b5713-e62b-40a5-88a2-9873d68430d9", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0},
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"88e57948-4690-4fd0-a0cf-a1633ca865ac", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"1106f860-761b-4d3f-be19-eb96d486f4ff", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"1fb47988-83be-427f-8225-bc31ce0224b2", "starting_time": 870, "ending_time": 875, delay: 0, cost: 0},
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"ad8e3680-bfd6-46a6-85e7-730ca1da8bd2", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"20e7f2c1-2b33-4b99-a7ce-4477aa44ce66", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"b465f9e2-f2cf-4635-bbab-a0fd93c5f067", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0},
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"4804b130-9387-417b-81a5-baa5cdd426bd", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"85519b02-2319-46ee-b693-94049e244c6c", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"a5102c5a-0700-4d44-bef0-8beee983f558", "starting_time": 910, "ending_time": 915, delay: 0, cost: 0},
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"732f9176-607a-43b2-b184-ffa49513b4ec", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"c8624196-8193-43af-968b-6ba8af4c9da3", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"66c7755a-733c-4cc1-8915-2a7a58d1cf7b", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0},
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"218c0a5a-5b6f-44e8-8f90-ff1e3175f441", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"4df5b617-1220-473d-a91c-1f07cca95c6b", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"e91cec6c-7a64-4020-a6d9-59d41dfb4aa0", "starting_time": 950, "ending_time": 955, delay: 0, cost: 0},
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"8fc35707-2e99-4d22-83ca-d6a18131f324", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"16eeb3f7-f924-4710-a6e8-00afef23c3c5", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"f99a18b9-7382-4852-8cda-67b860efc2e4", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0},
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"d51f9a02-dc37-48e0-9f33-bd415e0af845", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"6cfbb528-846f-4357-885b-1344f3e68f11", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"856876d5-293c-4e63-b20b-51216a79e478", "starting_time": 990, "ending_time": 995, delay: 0, cost: 0},
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"1134b61a-5699-4d27-8fbf-6a974966add8", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"315b2b59-3be3-48f4-b9e2-d9d00cdd422e", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"5afb82a3-d819-43de-aefd-0645f5c0cb2d", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0},
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"f0e574d4-9464-4659-871e-2202e0f1750f", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"519c1d61-4dcb-4e6a-9cb7-c1f7897bd67b", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"8e65811f-874b-403a-8eda-d6ebf685ca3c", "starting_time": 1030, "ending_time": 1035, delay: 0, cost: 0},
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"6a1bcd32-dc37-40a3-a823-de6a526d4a10", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"63139f07-35e4-4b8a-b764-7138508a92a1", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"3ce2f056-e489-4610-8236-fbf9d6a8e1e3", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0},
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"92361533-211a-4e9e-aefe-ac6bb4d84555", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"e38e84e9-96ad-407e-a8db-698654ed4013", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"26fae525-e1fa-4d15-9e56-7b08fe12bac8", "starting_time": 1070, "ending_time": 1075, delay: 0, cost: 0},
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"a893aa0a-af0a-48f7-8ed2-1fdcf4bfef03", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"3d2e3a7d-b87d-4ecb-a998-0fd024078b4c", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"72dfa9e8-f183-4087-b19b-21ec0f70c7ed", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0},
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"494eabdf-dbfe-4177-b06c-c7c46ff3ceba", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"2cb4994e-4f3f-4741-a510-512167cc18f5", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"8a3504e2-fc1e-49d7-aeef-cb39e174f4e5", "starting_time": 1110, "ending_time": 1115, delay: 0, cost: 0},
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"5b0fdd8a-c809-4115-b588-8e6cfb274d8f", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"35906900-71fa-4c20-bcbc-e4148b58bcca", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"1b33e348-2adf-4cd1-b711-19b12889a139", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0},
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"13cebab4-960a-4bd0-b4d8-93547830bd66", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"67088208-870a-4ff0-8490-6d4ab212ed53", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"7e68ef3f-2b8d-43b3-82e9-0edd2ae19f8d", "starting_time": 1150, "ending_time": 1155, delay: 0, cost: 0},
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"434b9bee-7076-4a6f-8dd7-30ebc1177458", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"c0917909-bd3f-4529-9424-4a547364d45b", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"772522d6-8df6-46e7-9484-7c9b1d56c8d7", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0},
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"e2d7dc38-9a92-476a-9d39-2ca8a56cdc20", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"75319270-831e-464b-9361-3a10086862ee", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"f009d662-04bf-4c86-833a-301eebe8cbc4", "starting_time": 1190, "ending_time": 1195, delay: 0, cost: 0},
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"632652f8-83a0-4955-bfa1-9d3ec4208a19", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"831bbd95-a6ef-4ffc-b71c-05b8ef6b53b9", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"60369984-2676-400b-8d42-23ec3af6f88f", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0},
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"05a0aa6d-3bb3-4dd4-bf2e-027b967ad1bf", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"595b7812-6e4b-4809-a3af-020ed6cb54e0", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"e14e3894-a55c-46a1-9210-49ef2cc17b79", "starting_time": 1230, "ending_time": 1235, delay: 0, cost: 0},
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"9630cfce-d433-4494-aad2-ce920eda4b00", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"0fe056a2-c462-4c15-878d-bab15ba6b4f3", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"3f087f7f-0ef1-4167-97a6-e470f7c73303", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0},
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"35ee5f65-a539-4004-8275-84988f7aa2de", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"2da36749-d31a-4979-bb5d-31c2bc5a51d2", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"4ea749f4-9695-491f-837a-64b202f19d28", "starting_time": 1270, "ending_time": 1275, delay: 0, cost: 0},
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"2fad020c-8c33-4c52-9f32-4f77b7d867d1", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"986ef04d-7ccc-43f5-b9a3-4ffc9bb6b57b", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"4e5f5b58-b2b4-4e5a-8260-caf3d8539f83", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0},
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"efe007bd-142d-4ba9-8d9e-a1c3e20f07e2", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"ade0d3f0-f534-43d4-afbd-bc4ba18e688f", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"aa13ec8e-bb4a-4856-be15-f60560f82d84", "starting_time": 1310, "ending_time": 1315, delay: 0, cost: 0},
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"e569d954-d161-4b97-a6b3-1d4ba16b1503", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"384469a8-8935-45ea-8f7c-9005eb615085", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"b1fbcd5f-d8c6-4fb0-9be1-79e6b78edcb6", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0},
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"ad7ea562-bf1b-45f2-a7ef-c02b87a1fab6", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"99f18e4d-fb0d-4e72-b12c-289cd3995f07", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"6db75c9b-98fd-4ce2-8481-b856580247aa", "starting_time": 1350, "ending_time": 1355, delay: 0, cost: 0},
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"7ff4ed15-875b-4fd7-85a5-b2fbdfe67629", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"a5abca7a-2e28-452c-a0ce-a8148401495d", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"661d7e85-f948-45db-91df-6438307e5f53", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0},
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"7b465d4b-d2db-48af-8099-4dce4dce9362", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"f67d4bf8-7da2-4376-b2ef-2f93260c1450", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"54881e54-dbef-4f1a-a2a7-dfab4db17528", "starting_time": 1390, "ending_time": 1395, delay: 0, cost: 0},
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"5a7c0db4-6cd1-4c66-b7d8-3842dfa50ab8", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"6cd27fc5-bab0-421d-ad26-87ba18608a20", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"e292dfad-8927-4995-ba1d-c83d5f5f306e", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0},
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"0ed4fcae-ae28-4211-bba4-c28e054ffc06", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"b6f0b451-5b64-423c-9f8b-2396941c9f3e", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"36281913-ee7b-4d5b-8cf5-25929bba8957", "starting_time": 1430, "ending_time": 1435, delay: 0, cost: 0},
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"4f78283c-09ac-43e2-96fe-993c25d38c72", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"b96fdb7d-a5ac-4e45-ab64-9b9e1db6eb15", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"eea08eb3-7d21-4114-9f2d-a2d02e3937d5", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0},
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"7e15c16b-8e4a-4285-b02d-15b50451208d", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"bdf2805d-ec7b-4909-b336-fbb4e7a6d3f2", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"e65feb87-1114-4fea-b3df-04a8a0d053ce", "starting_time": 1470, "ending_time": 1475, delay: 0, cost: 0},
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"07a911e7-3cfe-46ff-9155-b3e7e445afb2", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"f4ab8154-369a-4805-941d-a76eb1cadb67", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"c3ac30d2-70b0-495f-8443-bb6f9a9945f5", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0},
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"f96e972f-0f04-4407-a863-f825147eb94a", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"562e9ecc-6c31-4a99-a400-aba1cde92373", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"dff60bd5-f9d7-4f90-b1c0-03026f5378da", "starting_time": 1510, "ending_time": 1515, delay: 0, cost: 0},
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"410eed09-06a8-4c68-b1ea-f178b15d9523", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"63cac790-8718-472f-bea4-f408b9e115c5", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"e6e0e0e2-099d-4d02-9a86-067f85e09b9d", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0},
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"afeed65e-929a-4805-93b5-b5aeccef7c3b", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"85157bc1-7bd8-4e1e-ab53-340616ce9766", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"55e132d0-9468-470e-89ad-65a0c17a6e1b", "starting_time": 1550, "ending_time": 1555, delay: 0, cost: 0},
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"008ca6e9-e658-4386-ae40-2edadf5641b7", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"915e3ea6-91d1-44d1-8e42-b8724a6ec9c1", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"20994f7a-64bd-4fbb-b114-ab8e14f7f62a", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0},
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"4fa09c92-c3d1-411d-bf3a-eddd95ef8e6b", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"1729daac-4fb5-4fce-9ca9-d21e42cb87ca", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"16ca81fb-bf1d-4866-a544-d93733e4495a", "starting_time": 1590, "ending_time": 1595, delay: 0, cost: 0},
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"dde04ab7-c075-4fce-bf2b-8dffb76458c8", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"7caee51a-f6a1-4d05-b511-8ec9072e0835", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"95af4ba7-2b75-4968-aa4c-8e3f61467d23", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0},
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"a02d6725-8d62-4ad7-aa17-ab33d19cb2bc", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"572b1672-dd4e-40ac-8ed5-f8324ed611f6", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"475a5ee0-89b9-4cc4-831f-c243213e15ac", "starting_time": 1630, "ending_time": 1635, delay: 0, cost: 0},
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"efed6ea7-e040-4ef6-a9ee-62cb9a02891f", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"67424cbd-f5d0-42e8-b50e-27a9a36deb1f", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"a3b08b6e-cbd4-4b2a-b216-366b2db8a9e6", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0},
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"e7d998ee-3f2b-4776-a1e7-15a4ce0798cc", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"821d831a-aebd-41ca-8c56-f4e696fbb865", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"356b0fa0-ca63-4b28-9dd6-0c570bd7135a", "starting_time": 1670, "ending_time": 1675, delay: 0, cost: 0},
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"9ae8e3d6-14bb-4c1e-b29f-97d012e8c791", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"f0179826-47dc-4a86-aaa6-032bb9ae9a73", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"6dd2c691-1c73-4940-83d3-b3785ae37794", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0},
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"68c7f7c1-06ef-4676-aa2b-fb2c85f04d47", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"13dee716-a731-47fc-93ac-1c7e4ac417cb", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"4e20358b-550c-46e9-a95f-b037f3107c64", "starting_time": 1710, "ending_time": 1715, delay: 0, cost: 0},
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"8aba68fa-daf1-4d22-bde3-0faaac173918", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"f7f96f50-475a-4dac-9d3b-4ed3ad25a416", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"88d9b9d0-1329-4c6a-97cd-a16d69a59bee", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0},
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"253f1158-ac6d-48d7-a115-0718a3110b6a", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"830cd8b3-22d0-4114-9fe8-6018029998f4", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"23b5a000-c8f4-428a-840d-71ee03c88529", "starting_time": 1750, "ending_time": 1755, delay: 0, cost: 0},
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"0a28a510-6a84-4536-b468-bde611bbee09", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"7c8176de-3abb-4dd5-9802-7cd926e266a5", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"6bb49f6f-7cd3-4bbd-9604-e351bfe446f4", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0},
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"387a4738-aef6-42f0-96fd-8c1f644d5a7c", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"8be727e8-33ce-4039-8a93-db01dbdcdeda", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"63ac776e-d784-4cd6-8446-5fb54e292154", "starting_time": 1790, "ending_time": 1795, delay: 0, cost: 0},
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"adca064d-1fbf-4ce4-918a-caaee8559532", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"4609950e-92bc-46f6-ac7d-d23e513742ae", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"3bcc3370-4233-441c-bf77-4fb3eb9eb4bd", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0},
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"a716ebc6-79f2-4b94-ae5b-9736d4b286ad", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"9022c950-3149-4f64-9c59-3f58b65417f5", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"6e0c2248-1827-4e70-9afc-cbae8b68761f", "starting_time": 1830, "ending_time": 1835, delay: 0, cost: 0},
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"f4918573-57ff-42c0-8ef1-149a2f5099f7", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"c0c6e92f-9dd0-472c-9fab-3213395fdf6e", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"6496981a-d897-4b0a-baf6-1733ba5355c0", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0},
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"1568b778-0bc9-4265-ad4d-98e93e8eea31", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"82b49a7b-9512-4d9f-8d31-f1c002d0a574", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"0355b9ff-53b9-4eb8-950e-25c25103e10e", "starting_time": 1870, "ending_time": 1875, delay: 0, cost: 0},
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"aa2f907f-46d2-4fda-8787-03bd00b64a5d", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"a491779f-654e-4eb1-a3fd-ee5ae47c538f", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"a3aa960e-0113-411b-8635-154a8e84f900", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0},
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"1db19c4a-a13e-4fbd-952e-7ac73195af52", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"13d4e0e4-ceca-4138-9f46-eb9cb0336bca", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"620472b7-dfc5-4d55-a2c9-2ebe46386cc7", "starting_time": 1910, "ending_time": 1915, delay: 0, cost: 0},
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"ce9f13be-fb25-4904-899c-40f8daf3c1e2", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"ef4c08ad-575e-4b90-9f17-d1a00f1be798", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"eec6987b-5a1b-4cd4-9569-cd610facaf74", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0},
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"1a171a6c-fe67-4226-ab13-7dde68ae81ad", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"3261cdbb-2515-4c82-95ba-9b57d7b196cd", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"3d6fa914-2d90-4dd8-bcdc-2e7589ff6bfd", "starting_time": 1950, "ending_time": 1955, delay: 0, cost: 0},
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"23dfb5b2-f5a4-4823-92a5-a65f20d9c514", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"7cf8834c-2cee-4af6-82db-bc4c81d0c282", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"f9d82b7d-be6b-4788-bd41-16382920d25a", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0},
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"1f305539-4faf-4d0d-a3ed-deb8aacce7ad", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"ba0fddbf-dcd6-490e-8442-2a75f61c577d", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"4519e8a2-c966-4812-adb5-d712d26c305a", "starting_time": 1990, "ending_time": 1995, delay: 0, cost: 0}
]},
{label: "r2", times: [

]},
{label: "r3", times: [

]},
{label: "r4", times: [

]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"aaf633ae-a3ee-4767-9f1e-6aed04c1e19f", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"1d400097-8215-4eb2-95a1-2425f8d17a07", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"907b5c15-b744-4f98-bd4d-e469ea169f4b", "starting_time": 10, "ending_time": 15, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"471d9026-0584-404e-a7f3-fcb599874d8e", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"60902114-830e-40b6-9ec1-867157b30366", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"2682a9b9-e4a3-4422-9925-407228848386", "starting_time": 30, "ending_time": 35, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"5e31f4d7-aae8-46dd-865d-458a9fac38c4", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"caa39e5f-2f9f-43c6-9367-3d000a803421", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"d4681fee-9f56-42d0-b1a4-041c4fee3eea", "starting_time": 210, "ending_time": 215, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"4d1f6bfc-974d-4e58-80f8-570aae296d86", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"f335fb4e-47a4-4f08-9656-3f04df8e4d17", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"4f8bbd5e-1564-4252-97c1-f8e799c2727c", "starting_time": 230, "ending_time": 235, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"4189a5cd-8f2e-4fe4-84d3-51d41e981313", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"dc019a07-2f35-4437-ad5e-0c9a53e9a4dd", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"74a2a874-d32e-432e-877e-fb5c74d91ab2", "starting_time": 250, "ending_time": 255, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"3927f0bd-9fa5-4595-894f-6530b22c2c17", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"2eadbd86-e789-4add-96d7-9a499626f180", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"31ddbf23-0219-4523-9527-c6b0d3e2a762", "starting_time": 270, "ending_time": 275, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"7f204178-f9f2-484f-be8e-2b1f0d30e7b3", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"bcf54941-f926-46c3-8439-170b8587878c", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"69135402-4920-4322-b7be-bb3f630e29fb", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"cbbad666-0263-4a8f-ac75-114687009e6e", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"76fcd719-c421-4368-ba8b-200e0105be6f", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"4e6c3ce5-ae15-46a4-940f-d6c5b1c67b00", "starting_time": 310, "ending_time": 315, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"6bc4717d-0597-433f-94ab-4839b4dfd87e", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"56994018-5521-493d-9258-0635579b491b", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"15e7294d-473b-4b00-b816-626fe724e98d", "starting_time": 330, "ending_time": 335, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"aa76dbcc-d78a-4ec1-9723-002168bf93b5", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"5bc5dd4c-0e0c-44e3-843b-76f7567668e0", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"ccad4f7d-0158-475c-837e-3a291e8bd53c", "starting_time": 350, "ending_time": 355, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"0077c60f-c58c-43ab-84ed-c7609f33b426", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"e5f234bb-8197-4717-8e5d-19b1a967711c", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"0280d7c5-582e-47e7-8d53-2fc100f73bad", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"620b4e53-c99b-474b-8f6e-572546968ea4", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"3994dcd9-4c7b-4545-b0ec-946b7f252a42", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"21e803f6-cd73-4cc1-b663-48b057ee0203", "starting_time": 390, "ending_time": 395, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"09188d2b-1c8f-467b-a413-1f0c6f11ee11", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"81d45e39-9289-469b-be85-86fed258653e", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"59aa0704-adf9-4788-82a3-578c6ab2472b", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"e497dc64-7c32-46b7-ad2a-c3f242bcd967", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"01f97562-67f0-4554-b795-b07dc13421a8", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"7bf6721b-bdaf-4756-a49b-d06413e93cbc", "starting_time": 410, "ending_time": 415, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"ea56ede2-1780-4891-86ec-1b277092317e", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"976de207-f07b-4ad7-8286-30e20eabb151", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"68a8d7b4-2cc3-42db-8a4f-a5e2163a1625", "starting_time": 430, "ending_time": 435, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"13fa399d-3861-448b-a45e-24c9b2498986", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"b5ad8d20-d9d2-4f36-939a-008e86e0c64d", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"3b0a07ff-9773-4017-ae37-2d4884a4576c", "starting_time": 450, "ending_time": 455, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"05871da8-906a-4649-8717-cd705b1d077e", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"5d23a11d-3175-4c7e-aceb-edf00bd14326", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"f7b65c9b-08d2-452e-879c-d21e0db8e93d", "starting_time": 470, "ending_time": 475, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"0aa143c5-4907-4d93-ae6b-fab6dd44fda3", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"53cf2ec0-62cd-4c8a-beed-a3399e1738d5", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"876e684c-b51d-4fa5-a535-55a7814014f3", "starting_time": 490, "ending_time": 495, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"68f195ff-4ac1-407e-ab37-56b8529466f9", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"d5449c29-0ee6-46e3-9a7d-cc5234546607", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"a306dffd-da74-46c6-97f7-c1214243a421", "starting_time": 510, "ending_time": 515, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"2b252639-a945-4581-af6a-d6199426d36e", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"46161eff-30ea-4684-972d-0964218d6f03", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"2eb3d1a8-b08a-4a41-acbf-61a0895d33ad", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"acedd623-33f9-4f7f-9041-b745ddf45251", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"c633fd01-5b15-4fb1-84ad-55d1f7a96a94", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"54c76ee2-fab9-45e5-9e1f-da201c5bf810", "starting_time": 550, "ending_time": 555, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"25c63157-e386-4fa4-9c7b-a6257d6e7ed0", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"dc95b11f-99a1-4853-a3cf-6b34122a102a", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"d59c3385-97ea-4a24-b8c2-e654cf064a64", "starting_time": 570, "ending_time": 575, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"93b86958-c6dd-47f3-a6fb-47457cf1be77", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"9c2166fc-22fd-45bb-a7d1-598e24019171", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"8884ca9f-1ac0-4208-a0ee-ca35d34eb7f0", "starting_time": 590, "ending_time": 595, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"d091c867-6c78-49b3-b31d-ff8b6b020d13", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"8534b60f-59d6-43cd-ab85-88c7555c3e33", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"86c04265-04a6-4acb-8c57-0c53d40d9a7b", "starting_time": 70, "ending_time": 75, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"cfe9e5bf-61e2-401f-9c1e-92f3e83a9ade", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"a4443963-3175-408a-ba8f-e97d8b73dd02", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"b720448f-e21c-4f24-a410-51809679266b", "starting_time": 610, "ending_time": 615, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"6fbb7750-a660-4103-a534-255335fa6a7e", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"372dc35e-a301-4ff1-bd24-9406ac711c5f", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"3a2b3d4b-ecb7-4917-9131-93dbac2c086a", "starting_time": 630, "ending_time": 635, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"bf0a5fef-cccf-4751-b2ed-c7bf953adefb", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"deee92ff-9ed0-41d0-b8cb-e1946bd00eba", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"8865d475-5cf5-46d9-b02e-5d9bda20cbc2", "starting_time": 650, "ending_time": 655, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"4624c353-6b1a-4921-9d2e-51aa8c4bb331", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"c8a3e3f1-25be-4a95-9790-496bb6467ff6", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"c61b7fbd-d2f5-48f7-a89e-829fcb99b8ff", "starting_time": 670, "ending_time": 675, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"0ac13bad-b72d-4276-b733-8ad2085223a6", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"1dcb5352-b58b-438d-9408-8ad0c779ea52", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"48a80638-40b0-4d42-a00a-789053d36a46", "starting_time": 690, "ending_time": 695, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"501741ca-bf28-4896-9ab4-0cb05bceda35", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"3e81ba3f-f1a4-4167-b8f1-21f81d7b0107", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"fcc41607-7b6f-4727-b4ef-4aac444cb3a6", "starting_time": 710, "ending_time": 715, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"323f71cf-2afd-482f-8092-f7ffe483b55c", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"e55a14e7-2eaf-47ad-bef2-3447653b3918", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"b7ee2d2f-3236-4aa6-994c-3365a949f8d2", "starting_time": 730, "ending_time": 735, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"496a4291-ba19-4937-8560-ff7d38a92ef0", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"f1af7429-7111-4f15-8c47-0c0d340a8f41", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"8ca81918-3680-4876-b55f-f3e18adc0d13", "starting_time": 750, "ending_time": 755, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"a28c134e-0fcb-4ff9-946a-e406fcda9fbc", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"a908fdad-a6b0-4bf3-9cba-265c472b893a", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"18f35302-ce89-428e-b9ed-cb79545a7c74", "starting_time": 770, "ending_time": 775, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"76c511ee-18dd-47f8-9311-67df2813c37b", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"489948be-6102-408a-a1aa-39d95bfc88f0", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"209acbcd-80c3-4c97-a44b-df8574092fd6", "starting_time": 790, "ending_time": 795, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"c4100fd7-450f-4fe2-942b-340ba6c118fd", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"be41c7ce-343c-40c6-99be-72cfb35f9f13", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"7a717544-42a6-41c6-8879-a88f7a449d47", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"f49965ab-9d79-4a7c-ba1b-88962f88f633", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"e340569c-f2fb-4730-8200-1bbe671050c8", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"f21af81a-5228-4eb4-bd43-1c2b640beaef", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"ae757ffd-4660-4a5d-9226-837678d42037", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"5195ec5e-6953-4064-9590-755b0bbbda70", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"b2e7e749-e40d-4fd6-8887-e0304b0400b4", "starting_time": 830, "ending_time": 835, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"d1300fe7-c26c-4e8f-aca2-57bb53fee82c", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"11d1e190-ef7b-43a9-a6b5-b3365f717135", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"9c1b5713-e62b-40a5-88a2-9873d68430d9", "starting_time": 850, "ending_time": 855, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"88e57948-4690-4fd0-a0cf-a1633ca865ac", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"1106f860-761b-4d3f-be19-eb96d486f4ff", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"1fb47988-83be-427f-8225-bc31ce0224b2", "starting_time": 870, "ending_time": 875, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"ad8e3680-bfd6-46a6-85e7-730ca1da8bd2", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"20e7f2c1-2b33-4b99-a7ce-4477aa44ce66", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"b465f9e2-f2cf-4635-bbab-a0fd93c5f067", "starting_time": 890, "ending_time": 895, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"4804b130-9387-417b-81a5-baa5cdd426bd", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"85519b02-2319-46ee-b693-94049e244c6c", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"a5102c5a-0700-4d44-bef0-8beee983f558", "starting_time": 910, "ending_time": 915, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"732f9176-607a-43b2-b184-ffa49513b4ec", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"c8624196-8193-43af-968b-6ba8af4c9da3", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"66c7755a-733c-4cc1-8915-2a7a58d1cf7b", "starting_time": 930, "ending_time": 935, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"218c0a5a-5b6f-44e8-8f90-ff1e3175f441", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"4df5b617-1220-473d-a91c-1f07cca95c6b", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"e91cec6c-7a64-4020-a6d9-59d41dfb4aa0", "starting_time": 950, "ending_time": 955, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"8fc35707-2e99-4d22-83ca-d6a18131f324", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"16eeb3f7-f924-4710-a6e8-00afef23c3c5", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"f99a18b9-7382-4852-8cda-67b860efc2e4", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"d51f9a02-dc37-48e0-9f33-bd415e0af845", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"6cfbb528-846f-4357-885b-1344f3e68f11", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"856876d5-293c-4e63-b20b-51216a79e478", "starting_time": 990, "ending_time": 995, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"2310bc50-4b64-425d-a602-0f5434e87d61", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"54f227fb-0eed-445f-b51d-488a15e8ed1c", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"14f53b5e-dd5f-4523-845a-86f03ceed984", "starting_time": 110, "ending_time": 115, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"1134b61a-5699-4d27-8fbf-6a974966add8", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"315b2b59-3be3-48f4-b9e2-d9d00cdd422e", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"5afb82a3-d819-43de-aefd-0645f5c0cb2d", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"f0e574d4-9464-4659-871e-2202e0f1750f", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"519c1d61-4dcb-4e6a-9cb7-c1f7897bd67b", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"8e65811f-874b-403a-8eda-d6ebf685ca3c", "starting_time": 1030, "ending_time": 1035, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"6a1bcd32-dc37-40a3-a823-de6a526d4a10", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"63139f07-35e4-4b8a-b764-7138508a92a1", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"3ce2f056-e489-4610-8236-fbf9d6a8e1e3", "starting_time": 1050, "ending_time": 1055, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"92361533-211a-4e9e-aefe-ac6bb4d84555", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"e38e84e9-96ad-407e-a8db-698654ed4013", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"26fae525-e1fa-4d15-9e56-7b08fe12bac8", "starting_time": 1070, "ending_time": 1075, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"a893aa0a-af0a-48f7-8ed2-1fdcf4bfef03", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"3d2e3a7d-b87d-4ecb-a998-0fd024078b4c", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"72dfa9e8-f183-4087-b19b-21ec0f70c7ed", "starting_time": 1090, "ending_time": 1095, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"494eabdf-dbfe-4177-b06c-c7c46ff3ceba", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"2cb4994e-4f3f-4741-a510-512167cc18f5", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"8a3504e2-fc1e-49d7-aeef-cb39e174f4e5", "starting_time": 1110, "ending_time": 1115, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"5b0fdd8a-c809-4115-b588-8e6cfb274d8f", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"35906900-71fa-4c20-bcbc-e4148b58bcca", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"1b33e348-2adf-4cd1-b711-19b12889a139", "starting_time": 1130, "ending_time": 1135, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"13cebab4-960a-4bd0-b4d8-93547830bd66", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"67088208-870a-4ff0-8490-6d4ab212ed53", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"7e68ef3f-2b8d-43b3-82e9-0edd2ae19f8d", "starting_time": 1150, "ending_time": 1155, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"434b9bee-7076-4a6f-8dd7-30ebc1177458", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"c0917909-bd3f-4529-9424-4a547364d45b", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"772522d6-8df6-46e7-9484-7c9b1d56c8d7", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"e2d7dc38-9a92-476a-9d39-2ca8a56cdc20", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"75319270-831e-464b-9361-3a10086862ee", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"f009d662-04bf-4c86-833a-301eebe8cbc4", "starting_time": 1190, "ending_time": 1195, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"361b0205-fffa-4f33-aa8d-8cc0c9df2c69", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"204ca0e1-72f9-4ba9-9683-31d2f1544e4e", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"59d3c4e7-3643-4c88-a91f-61aacc8d150e", "starting_time": 130, "ending_time": 135, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"632652f8-83a0-4955-bfa1-9d3ec4208a19", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"831bbd95-a6ef-4ffc-b71c-05b8ef6b53b9", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"60369984-2676-400b-8d42-23ec3af6f88f", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"05a0aa6d-3bb3-4dd4-bf2e-027b967ad1bf", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"595b7812-6e4b-4809-a3af-020ed6cb54e0", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"e14e3894-a55c-46a1-9210-49ef2cc17b79", "starting_time": 1230, "ending_time": 1235, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"9630cfce-d433-4494-aad2-ce920eda4b00", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"0fe056a2-c462-4c15-878d-bab15ba6b4f3", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"3f087f7f-0ef1-4167-97a6-e470f7c73303", "starting_time": 1250, "ending_time": 1255, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"35ee5f65-a539-4004-8275-84988f7aa2de", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"2da36749-d31a-4979-bb5d-31c2bc5a51d2", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"4ea749f4-9695-491f-837a-64b202f19d28", "starting_time": 1270, "ending_time": 1275, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"2fad020c-8c33-4c52-9f32-4f77b7d867d1", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"986ef04d-7ccc-43f5-b9a3-4ffc9bb6b57b", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"4e5f5b58-b2b4-4e5a-8260-caf3d8539f83", "starting_time": 1290, "ending_time": 1295, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"efe007bd-142d-4ba9-8d9e-a1c3e20f07e2", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"ade0d3f0-f534-43d4-afbd-bc4ba18e688f", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"aa13ec8e-bb4a-4856-be15-f60560f82d84", "starting_time": 1310, "ending_time": 1315, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"e569d954-d161-4b97-a6b3-1d4ba16b1503", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"384469a8-8935-45ea-8f7c-9005eb615085", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"b1fbcd5f-d8c6-4fb0-9be1-79e6b78edcb6", "starting_time": 1330, "ending_time": 1335, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"ad7ea562-bf1b-45f2-a7ef-c02b87a1fab6", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"99f18e4d-fb0d-4e72-b12c-289cd3995f07", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"6db75c9b-98fd-4ce2-8481-b856580247aa", "starting_time": 1350, "ending_time": 1355, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"7ff4ed15-875b-4fd7-85a5-b2fbdfe67629", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"a5abca7a-2e28-452c-a0ce-a8148401495d", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"661d7e85-f948-45db-91df-6438307e5f53", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"7b465d4b-d2db-48af-8099-4dce4dce9362", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"f67d4bf8-7da2-4376-b2ef-2f93260c1450", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"54881e54-dbef-4f1a-a2a7-dfab4db17528", "starting_time": 1390, "ending_time": 1395, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"f97eeb53-c408-47da-8979-2214ad3b07ae", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"2987e347-0f51-4a89-aa4f-13bfe6169f67", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"8b79c801-6880-4c38-84c0-506711467d8e", "starting_time": 150, "ending_time": 155, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"5a7c0db4-6cd1-4c66-b7d8-3842dfa50ab8", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"6cd27fc5-bab0-421d-ad26-87ba18608a20", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"e292dfad-8927-4995-ba1d-c83d5f5f306e", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"0ed4fcae-ae28-4211-bba4-c28e054ffc06", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"b6f0b451-5b64-423c-9f8b-2396941c9f3e", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"36281913-ee7b-4d5b-8cf5-25929bba8957", "starting_time": 1430, "ending_time": 1435, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"4f78283c-09ac-43e2-96fe-993c25d38c72", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"b96fdb7d-a5ac-4e45-ab64-9b9e1db6eb15", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"eea08eb3-7d21-4114-9f2d-a2d02e3937d5", "starting_time": 1450, "ending_time": 1455, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"7e15c16b-8e4a-4285-b02d-15b50451208d", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"bdf2805d-ec7b-4909-b336-fbb4e7a6d3f2", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"e65feb87-1114-4fea-b3df-04a8a0d053ce", "starting_time": 1470, "ending_time": 1475, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"07a911e7-3cfe-46ff-9155-b3e7e445afb2", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"f4ab8154-369a-4805-941d-a76eb1cadb67", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"c3ac30d2-70b0-495f-8443-bb6f9a9945f5", "starting_time": 1490, "ending_time": 1495, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"f96e972f-0f04-4407-a863-f825147eb94a", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"562e9ecc-6c31-4a99-a400-aba1cde92373", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"dff60bd5-f9d7-4f90-b1c0-03026f5378da", "starting_time": 1510, "ending_time": 1515, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"410eed09-06a8-4c68-b1ea-f178b15d9523", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"63cac790-8718-472f-bea4-f408b9e115c5", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"e6e0e0e2-099d-4d02-9a86-067f85e09b9d", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"afeed65e-929a-4805-93b5-b5aeccef7c3b", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"85157bc1-7bd8-4e1e-ab53-340616ce9766", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"55e132d0-9468-470e-89ad-65a0c17a6e1b", "starting_time": 1550, "ending_time": 1555, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"008ca6e9-e658-4386-ae40-2edadf5641b7", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"915e3ea6-91d1-44d1-8e42-b8724a6ec9c1", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"20994f7a-64bd-4fbb-b114-ab8e14f7f62a", "starting_time": 1570, "ending_time": 1575, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"4fa09c92-c3d1-411d-bf3a-eddd95ef8e6b", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"1729daac-4fb5-4fce-9ca9-d21e42cb87ca", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"16ca81fb-bf1d-4866-a544-d93733e4495a", "starting_time": 1590, "ending_time": 1595, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"a5da92d8-99d7-4c03-a15d-2c1a17b757e0", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"d03a3830-5267-4c83-8eeb-f47cf1b82790", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"413570f6-a091-4e2c-a78f-5024b35d92f4", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"dde04ab7-c075-4fce-bf2b-8dffb76458c8", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"7caee51a-f6a1-4d05-b511-8ec9072e0835", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"95af4ba7-2b75-4968-aa4c-8e3f61467d23", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"a02d6725-8d62-4ad7-aa17-ab33d19cb2bc", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"572b1672-dd4e-40ac-8ed5-f8324ed611f6", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"475a5ee0-89b9-4cc4-831f-c243213e15ac", "starting_time": 1630, "ending_time": 1635, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"efed6ea7-e040-4ef6-a9ee-62cb9a02891f", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"67424cbd-f5d0-42e8-b50e-27a9a36deb1f", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"a3b08b6e-cbd4-4b2a-b216-366b2db8a9e6", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"e7d998ee-3f2b-4776-a1e7-15a4ce0798cc", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"821d831a-aebd-41ca-8c56-f4e696fbb865", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"356b0fa0-ca63-4b28-9dd6-0c570bd7135a", "starting_time": 1670, "ending_time": 1675, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"9ae8e3d6-14bb-4c1e-b29f-97d012e8c791", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"f0179826-47dc-4a86-aaa6-032bb9ae9a73", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"6dd2c691-1c73-4940-83d3-b3785ae37794", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"68c7f7c1-06ef-4676-aa2b-fb2c85f04d47", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"13dee716-a731-47fc-93ac-1c7e4ac417cb", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"4e20358b-550c-46e9-a95f-b037f3107c64", "starting_time": 1710, "ending_time": 1715, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"8aba68fa-daf1-4d22-bde3-0faaac173918", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"f7f96f50-475a-4dac-9d3b-4ed3ad25a416", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"88d9b9d0-1329-4c6a-97cd-a16d69a59bee", "starting_time": 1730, "ending_time": 1735, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"253f1158-ac6d-48d7-a115-0718a3110b6a", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"830cd8b3-22d0-4114-9fe8-6018029998f4", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"23b5a000-c8f4-428a-840d-71ee03c88529", "starting_time": 1750, "ending_time": 1755, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"0a28a510-6a84-4536-b468-bde611bbee09", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"7c8176de-3abb-4dd5-9802-7cd926e266a5", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"6bb49f6f-7cd3-4bbd-9604-e351bfe446f4", "starting_time": 1770, "ending_time": 1775, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"387a4738-aef6-42f0-96fd-8c1f644d5a7c", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"8be727e8-33ce-4039-8a93-db01dbdcdeda", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"63ac776e-d784-4cd6-8446-5fb54e292154", "starting_time": 1790, "ending_time": 1795, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"5c8c9246-4f94-4245-901f-14c84a0abfb7", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"59935fdd-2554-4762-8156-e63e798179aa", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"cbc86395-95b1-462f-813a-5316c6746fdf", "starting_time": 190, "ending_time": 195, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"adca064d-1fbf-4ce4-918a-caaee8559532", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"4609950e-92bc-46f6-ac7d-d23e513742ae", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"3bcc3370-4233-441c-bf77-4fb3eb9eb4bd", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"a716ebc6-79f2-4b94-ae5b-9736d4b286ad", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"9022c950-3149-4f64-9c59-3f58b65417f5", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"6e0c2248-1827-4e70-9afc-cbae8b68761f", "starting_time": 1830, "ending_time": 1835, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"f4918573-57ff-42c0-8ef1-149a2f5099f7", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"c0c6e92f-9dd0-472c-9fab-3213395fdf6e", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"6496981a-d897-4b0a-baf6-1733ba5355c0", "starting_time": 1850, "ending_time": 1855, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"1568b778-0bc9-4265-ad4d-98e93e8eea31", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"82b49a7b-9512-4d9f-8d31-f1c002d0a574", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"0355b9ff-53b9-4eb8-950e-25c25103e10e", "starting_time": 1870, "ending_time": 1875, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"aa2f907f-46d2-4fda-8787-03bd00b64a5d", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"a491779f-654e-4eb1-a3fd-ee5ae47c538f", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"a3aa960e-0113-411b-8635-154a8e84f900", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"1db19c4a-a13e-4fbd-952e-7ac73195af52", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"13d4e0e4-ceca-4138-9f46-eb9cb0336bca", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"620472b7-dfc5-4d55-a2c9-2ebe46386cc7", "starting_time": 1910, "ending_time": 1915, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"ce9f13be-fb25-4904-899c-40f8daf3c1e2", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"ef4c08ad-575e-4b90-9f17-d1a00f1be798", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"eec6987b-5a1b-4cd4-9569-cd610facaf74", "starting_time": 1930, "ending_time": 1935, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"1a171a6c-fe67-4226-ab13-7dde68ae81ad", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"3261cdbb-2515-4c82-95ba-9b57d7b196cd", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"3d6fa914-2d90-4dd8-bcdc-2e7589ff6bfd", "starting_time": 1950, "ending_time": 1955, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"23dfb5b2-f5a4-4823-92a5-a65f20d9c514", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"7cf8834c-2cee-4af6-82db-bc4c81d0c282", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"f9d82b7d-be6b-4788-bd41-16382920d25a", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"1f305539-4faf-4d0d-a3ed-deb8aacce7ad", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"ba0fddbf-dcd6-490e-8442-2a75f61c577d", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"4519e8a2-c966-4812-adb5-d712d26c305a", "starting_time": 1990, "ending_time": 1995, delay: 0, cost: 0}
]},
];
