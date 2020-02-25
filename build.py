class Compile:
	def __init__(self):
		self.run()

	def run(self):
		import os
		os.system("cd Turok && gradlew setupDecompWorkspace --stop && gradlew clean build")
		
		import shutil
		shutil.copyfile("Turok/build/libs/turok-b0.1.jar", os.getenv("APPDATA") + "\\.minecraft\\mods\\turok-b0.1.jar")

		import sys
		sys.exit()

Compile()