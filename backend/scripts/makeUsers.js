token =
	"eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhcnlhIiwic3ViIjoic2FtcGxlMUBnbWFpbC5jb20iLCJleHAiOjE3MTM4MTEzNDIsImlhdCI6MTcxMzgwOTU0Miwic2NvcGUiOiJSRUFEIERFTEVURSBVUERBVEUgV1JJVEUifQ.uh2RWKisr3KPo06I8yvuSwJkho_uaCQk1Ej-8SqtaZ6JF6uJpaesG92pL3yBI2wSIlI1o9DUA6zA36KHZs3xopBXV8fj0GvxOd7MNwveAnHePInz8ci9_cS7UuqWoEDzPm3jipTMNUgRO33VXVVM5SostW_tfbQAiJ2Rn237bv94w_bkKdzUQbM373Gyis4z26dxKDZdWH9wlpebWyrs385oyEj7gUqOOAEH-OJkUr_YbdVYGe6kHgd00FUhy825qgMR7bc1zKje2Kri3Q3-3Tuvi4f2jvThgtUSV2lwq461HlOZbB4jexTq6lI7jXX67MnKEohc9N4NCwwGFoRcrg";

async function getUsernameorPassword() {
	try {
		const res = await fetch(
			"https://frightanic.com/goodies_content/docker-names.php"
		);
		const data = await res.text();
		const password = data.split("\n")[0].trim();
		return password;
	} catch (error) {
		console.log(error);
	}
}

async function getName() {
	try {
		const res = await fetch("https://api.namefake.com");
		const data = await res.json();
		return data.name;
	} catch (error) {
		console.log(error);
	}
}

async function insertUser(user) {
	try {
		const res = await fetch("http://localhost:8080/api/users", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${token}`,
			},
			body: JSON.stringify(user),
		});
		const data = await res.json();
		if (res.status == 200) {
			console.log("User created successfully");
			return true;
		} else {
			console.log("Error: Unable to create user");
			return false;
		}
	} catch (error) {
		console.log(error);
		return false;
	}
}

function saveCSV(userList) {
	const csv = userList
		.map(
			(user) =>
				`${user.name},${user.username},${user.password},${user.email},${user.roles}`
		)
		.join("\n");
	console.log(csv);
	// Save the csv to a file
	const fs = require("fs");
	fs.writeFileSync("users.csv", csv);
}

async function main() {
	// Loop through 500 times to create 500 users

    const userList = [];
    
    // take system input for number of users    

	for (let i = 0; i < 500; i++) {
		const name = await getName();
		const username = await getUsernameorPassword();
		const password = await getUsernameorPassword();

		if (
			username === undefined ||
			password === undefined ||
			name === undefined
		) {
			console.log("Error: Unable to get username or password");
			continue;
		}
		const email = `${username}@gmail.com`;
		const user = {
			name,
			username,
			password,
			email,
			roles: "ROLE_ADMIN",
		};
		const check = insertUser(user);
		if (!check) {
			console.log("Error: Unable to create user");
			continue;
		}
		console.log(`User ${i} created successfully`);
		userList.push(user);
    }
    
	saveCSV(userList);
}

main();
