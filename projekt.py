from smbus2 import SMBus
from mlx90614 import MLX90614
from time import sleep
import json
import datetime
import speech_recognition as sr
import tkinter as tk
from gtts import gTTS
import os

recognizer = sr.Recognizer()



def speech_to_text():

    with sr.Microphone() as source:

        print("Proszę poczekać")
        #speak_text("Proszę poczekać", "pl")
        recognizer.adjust_for_ambient_noise(source, duration=6)

        counter = 1

        while counter <= 2:
            print("Proszę podać swoje imię i nazwisko")
            #speak_text("Proszę podać swoje imię i nazwisko", "pl")


            

            try:
                audio_data = recognizer.listen(source, timeout=6, phrase_time_limit = 8)
                text = recognizer.recognize_google(audio_data, language='pl-PL')
                #print(recognizer.energy_threshold)
                
                if len(text.split(" ")) != 2 :
                    print("Podaj po max 1 slowie na imie i nazwisko")
                    counter += 1
                    continue
                    
                
                    

                while True:
                    print("Czy to twoje imię i nazwisko: " + text + " (Odpowiedz 'tak' lub 'nie')")
                    answer_audio = recognizer.listen(source, timeout=3, phrase_time_limit = 5)
                    answer = recognizer.recognize_google(answer_audio, language='pl-PL').lower()
                    print(answer)
                    try:
                        if answer == "tak":
                            return text.split(" ")
                        elif answer == "nie":
                            print("Proszę spróbować jeszcze raz.")
                            break
                        else:
                            print("Odpowiedz tak lub nie")
                            continue
                    except Exception:
                        continue
            except sr.UnknownValueError:
                print("Nie rozumiem mowy")
            except sr.WaitTimeoutError:
                print("Czas upłynął, spróbuj ponownie")
            except sr.RequestError as e:
                print("Błąd serwera; {0}".format(e))
            except:
                print("UPS")

            counter += 1
          


def spell_first_name():

    while True:
        with sr.Microphone() as source:
            print("Proszę przeliterować imię.")
            audio_data = recognizer.listen(source, timeout=6, phrase_time_limit = 8)
            #print(recognizer.energy_threshold)
            try:
                text = recognizer.recognize_google(audio_data, language='pl-PL')
                name = text.replace(" ", "")
                # print("Usłyszane: ", imie)
                return name
            except sr.UnknownValueError or sr.WaitTimeoutError:
                print("Nie rozumiem mowy")
            except sr.RequestError as e:
                print("Błąd serwera; {0}".format(e))
            except:
                print("UPS")
                
def spell_last_name():
    while True:
        with sr.Microphone() as source:
            print("Proszę przeliterować nazwisko.")
            audio_data = recognizer.listen(source, timeout=6, phrase_time_limit = 8)
            #print(recognizer.energy_threshold)

            try:
                text = recognizer.recognize_google(audio_data, language='pl-PL')
                surname = text.replace(" ", "")
                # print("Usłyszane:", nazwisko)
                return surname

            except sr.UnknownValueError or sr.WaitTimeoutError:
                print("Nie rozumiem mowy")
            except sr.RequestError as e:
                print("Błąd serwera; {0}".format(e))
            except:
                print("UPS")    
                
                
def is_your_full_name(name, surname):
    
    name = name.title()
    surname = surname.title()
    full_name = name + " " + surname
    
    with sr.Microphone() as source:
        recognizer.adjust_for_ambient_noise(source, duration=6)
        #print(recognizer.energy_threshold)
        print("Czy to twoje dane: " + full_name + "?  (Odpowiedz 'tak' lub 'nie')")
        audio_data = recognizer.listen(source, timeout=5, phrase_time_limit = 8)
        
        try:
            answer1 = recognizer.recognize_google(audio_data, language='pl-PL').lower()
            if answer1 == "tak":
                print("Dziękuje, to wszystko")
                return [name,surname]
            else:
                print(answer1)
                print("Skontaktuj się z personelem, aby uzupełnił twoje dane.")
        except sr.UnknownValueError:
            print("Unknown error")
        except sr.WaitTimeoutError:
            print("Timeout error")
        except sr.RequestError as e:
            print("Błąd serwera; {0}".format(e))

        
            
def save_to_file(full_name):
    f = open("name.txt", "w", encoding="utf-8")
    f.write(full_name)
    f.close()
    print("Dane zapisane do pliku")



def accuracy(amb,obj):
    if(amb>=0 and amb<=50 and obj>=0 and obj <=60):
        return 0.5
    else:
        return 1.0
        
def load_json():
    
    with open("config.cfg", "r") as file:
        config = json.load(file)
            
        for k in config:
            config[k] = float(config[k])
        return config

def load_temperatures(config):
    
    TEMP_BELOW_AVG = config["TEMP_BELOW_AVG"]
    TEMP_FEVER = config["TEMP_FEVER"]
    TEMP_CRITICAL = config["TEMP_CRITICAL"]
    TEMP_CORRECTION = config["TEMP_CORRECTION"]
    EPSILON = config["EPSILON"]
    
    return TEMP_BELOW_AVG, TEMP_FEVER, TEMP_CRITICAL, TEMP_CORRECTION, EPSILON


# Function to create and play text-to-speech audio
def speak_text(text, language):
    # Passing the text and language to the engine
    myobj = gTTS(text=text, lang=language, slow=False)
    # Saving the converted audio in a mp3 file named filename
    myobj.save("output.mp3")
    # Playing the converted file
    #os.system(f"start {filename}")
    os.system("mpv output.mp3")
    sleep(8)



if __name__ == "__main__":
    bus = SMBus(1)
    sensor = MLX90614(bus, address=0x5A)
    
    config = load_json()
    TEMP_BELOW_AVG, TEMP_FEVER, TEMP_CRITICAL, TEMP_CORRECTION, EPSILON = load_temperatures(config)

    i = 0
    temp_list = []
    if_meas = False
    print("Start urzadzenia")
    while True:
        
        amb = sensor.get_amb_temp()
        sensor_temp = sensor.get_obj_temp()-TEMP_CORRECTION
        obj = (((sensor_temp**4 - (1 - EPSILON) * amb**4)/EPSILON)**(1/4)) + 1.5
        
        ##print(f"Ambient Temperature: {amb}")
            
        if abs(amb - obj) > 5.0 and if_meas == False:
            print("Wykryto pacjenta!")
            print("Proszę pozostać w bezruchu...")
            print("0s")
            if_meas = True
        
        if if_meas:
            if abs(amb - obj) > 5.0:
                if i == 0:
                    i += 1
                    sleep(0.2)
                elif i == 150:
                    avg = sum(temp_list)/(len(temp_list))
                    print("3s")
                    if avg < TEMP_BELOW_AVG:
                        print(f"Temperatura wynosi: {avg:.2f}. Jest poniżej normy.")
                    elif avg > TEMP_FEVER and avg < TEMP_CRITICAL:
                        print(f"Temperatura wynosi: {avg:.2f}. Masz gorączkę.")
                    elif avg > TEMP_CRITICAL:
                        print(f"Temperatura wynosi: {avg:.2f}. Stan krytyczny.")
                    else:
                        print(f"Temperatura wynosi: {avg:.2f}")
                        
                    #tutaj prosimy  o imie i nazwisko glosowo
                    
                    full_name = speech_to_text()

                    if full_name is None:
                        name = spell_first_name()
                        surname = spell_last_name()
                        full_name = is_your_full_name(name, surname)
                        if full_name is None:
                            pass
                        
                        
                    
                    
                    
                    with open("dane.csv", "a") as file:
                        acc = accuracy(amb, avg)
                        time = datetime.datetime.now().strftime("%Y,%m,%d,%H,%M,%S")
                        if full_name is None:
                            temp=f"{avg:.2f},{amb:.2f},{time},{acc}\n"
                        else:
                            name = full_name[0]
                            surname = full_name[1]
                            temp=f"{avg:.2f},{amb:.2f},{time},{acc},{name},{surname}\n"
                        file.write(temp)
                    if avg < TEMP_BELOW_AVG:
                        print(f"Temperatura wynosi: {avg:.2f}. Jest poniżej normy.")
                    elif avg > TEMP_FEVER and avg < TEMP_CRITICAL:
                        print(f"Temperatura wynosi: {avg:.2f}. Masz gorączkę.")
                    elif avg > TEMP_CRITICAL:
                        print(f"Temperatura wynosi: {avg:.2f}. Stan krytyczny.")
                    else:
                        print(f"Temperatura wynosi: {avg:.2f}")
                    print("Zapisano dane")
                    print((datetime.datetime.now()))
                    print("----------------------------------------------")
                    i = 0
                    temp_list = []
                    if_meas = False
                    sleep(5)
                elif i == 50:
                    print("1s")
                    i += 1
                    sleep(0.02)
                    
                elif i == 100:
                    print("2s")
                    i += 1
                    sleep(0.02)
                    
                else:
                    temp_list.append(obj)
                    i += 1
                    sleep(0.02)
    #         print(i)
    #         print(temp_list)
            else:
                print("Wracaj Panie Pacjencie! Wykonaj pomiar od nowa.")
                #feuer!
                if_meas = False
                i = 0
                temp_list = []
                sleep(4)
                
                
           
        else:    
            sleep(1)
    bus.close()
