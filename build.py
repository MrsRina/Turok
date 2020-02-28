class Compile:
	def __init__(self):
		self.run()

	def run(self):
		import os
		os.system("cd Turok && gradlew setupDecompWorkspace --stop && gradlew clean build")
		
		import shutil
		try:
			shutil.copyfile("Turok/build/libs/turok-b0.2-release.jar", os.getenv("APPDATA") + "\\.minecraft\\mods\\turok-b0.2-release.jar")
			os.system("start C:/Users/Public/Desktop/Minecraft_Launcher")
			print("Copiadokkk")
		except:
			print("Ta sem o lib fodase")

		import sys
		sys.exit()

Compile()