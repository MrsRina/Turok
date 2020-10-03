class Compile:
	def __init__(self):
		self.run()

	def run(self):
		import os
		os.system("cd Turok && ./gradlew setupDecompWorkspace --stop && ./gradlew clean build")
		
		import shutil
		try:
			shutil.copyfile("Turok/build/libs/turok-0.5-release.jar", + "/home/yourusergoeshere/.minecraft/mods/turok-client-0.5.jar")
			print("Copiadokkk")
		except:
			print("Ta sem o lib fodase")

		import sys
		sys.exit()

Compile()