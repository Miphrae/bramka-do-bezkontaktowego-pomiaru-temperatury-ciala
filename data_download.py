import subprocess
import os
from time import sleep


def send_file(file_source: str, destination: str):
    scp_command = f"scp {file_source} {destination}"
    try:
        subprocess.run(scp_command, shell=True, check=True)
        print(f"File {file_source} transferred successfully.")
    except subprocess.CalledProcessError as e:
        print(f"Error occurred while transferring file: {e}")


if __name__ == "__main__":

    dane = []
    new_dane = []
    number_of_files = 0

    flag_work = True
    while flag_work:
        send_file("team15@192.168.0.91:~/Desktop/bramka_pomiarowa/dane.csv",
                  "C:\\PW\\Proj_gr\\Python_dane\\dane_combined")
        try:
            with open("C:\\PW\\Proj_gr\\Python_dane\\dane_combined\\dane.csv", "r") as file:
                new_dane = [el.strip() for el in file]
                file.close()
        except:
            print("nie odnaleziono pliku :(")

        new_files_number = 0
        new_files_number = len(new_dane) - len(dane)
        flag_found = False
        if new_files_number != 0:
            flag_found = True
        print(new_files_number)
        ##dane = new_dane[:]
        # print(len(new_dane))
        # print(new_files_number)

        for i in range(new_files_number):
            # for i in range(5):
            with open(f"C:\\PW\\Proj_gr\\Python_dane\\dane\\plik_{number_of_files}.csv", "w") as file:
                file.write(new_dane[len(dane) - 1 + i + 1])
                file.close()
            number_of_files += 1

        dane = new_dane[:]

        if flag_found:
            sleep(5)
            print("dzialam w stanie: active")
        else:
            sleep(10)
            print("dzialam w stanie: idle")
