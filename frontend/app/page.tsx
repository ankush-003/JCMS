"use client";

import { on } from "events";
import Image from "next/image";
import { useState } from "react";

const initState = {
	username: "",
	email: "",
	password: "",
};

export default function Home() {
	const [deets, setDeets] = useState(initState);

	const [view, setView] = useState(0);

	function onLogin(e: React.FormEvent<HTMLFormElement>) {
		e.preventDefault();

		console.log(deets);
	}

	function onRegister(e: React.FormEvent<HTMLFormElement>) {
		e.preventDefault();

		console.log(deets);
	}

	function clearDeets() {
		setDeets(initState);
	}

	function toggleView() {
		clearDeets();
		setView((view) => (view === 0 ? 1 : 0));
	}

	return (
		<div className="flex flex-col h-screen gap-10 items-center justify-start font-poppins border p-10">
			<div className="flex flex-col gap-10 items-center justify-start  ">
				<div>
					<div className=" text-primary font-bold text-4xl">
						JASMA
					</div>
					<div className=" text-slate-700 font-semibold">
						Just another social media application
					</div>
				</div>
				<div className="flex flex-col items-center gap-4 w-80 border-2 border-slate-400 p-4 rounded-xl">
					<div className="text-2xl font-bold text-slate-700">
						{view == 0 ? "LOGIN" : "REGISTER"}
					</div>
					<form
						className="flex flex-col gap-2 w-full"
						onSubmit={view == 0 ? onLogin : onRegister}
					>
						<div className="flex flex-col gap-4">
							<div className="flex flex-col">
								<label className="text-sm">Email</label>
								<input
									type="email"
									placeholder="Email"
									required
									className=" focus:outline-none bg-slate-200 p-2 rounded-md"
									value={deets.email}
									onChange={(e) =>
										setDeets({
											...deets,
											email: e.target.value,
										})
									}
								/>
							</div>
							{view == 1 && (
								<div className="flex flex-col">
									<label className="text-sm">Username</label>
									<input
										type="username"
										placeholder="Username"
										required
										className=" focus:outline-none bg-slate-200 p-2 rounded-md"
										value={deets.username}
										onChange={(e) =>
											setDeets({
												...deets,
												username: e.target.value,
											})
										}
									/>
								</div>
							)}
							<div className="flex flex-col">
								<label className="text-sm">Password</label>
								<input
									type="password"
									placeholder="Password"
									required
									className=" focus:outline-none bg-slate-200 p-2 rounded-md"
									value={deets.password}
									onChange={(e) =>
										setDeets({
											...deets,
											password: e.target.value,
										})
									}
								/>
							</div>
						</div>
						{view == 0 ? (
							<div className="text-center text-sm">
								Don&apos;t have an account?{" "}
								<button
									onClick={toggleView}
									className="text-primary"
								>
									Register
								</button>
							</div>
						) : (
							<div className="text-center text-sm">
								Already have an account?{" "}
								<button
									onClick={toggleView}
									className="text-primary"
								>
									Login
								</button>
							</div>
						)}
						<button
							type="submit"
							className="bg-primary rounded-xl p-2 mt-8 font-bold text-white"
						>
							{view == 0 ? "LOGIN" : "REGISTER"}
						</button>
					</form>
				</div>
			</div>
		</div>
	);
}
