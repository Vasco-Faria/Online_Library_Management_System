from faker import Faker
import random
from datetime import datetime,timedelta
import pika
import time
import json
import requests
from threading import Thread


fake=Faker()


time.sleep(30)

class User:
    def __init__(self, user_id, name,senha,email,tipo,phonenumber, birthdate):
        self.user_id = user_id
        self.name = name
        self.email = email
        self.senha=senha
        self.tipo=tipo
        self.phonenumber=phonenumber
        self.birthdate = birthdate
        
    def to_dict(self):
        return {
            "tipo":"User",
            "user_id": self.user_id,
            "name": self.name,
            "email": self.email,
            "senha":self.senha,
            "tipo": self.tipo,
            "phonenumber":self.phonenumber,
            "birthdate": str(self.birthdate),
        }

class Book:
    def __init__(self,book_id,title,authors):
        self.id=book_id
        self.title=title
        self.authors=authors
        
    @staticmethod
    def get_random_book(max_retries=3):
        base_url = "https://www.googleapis.com/books/v1/volumes"
        subjects = ["fiction", "history", "science", "biography", "fantasy"]
        random_subject = random.choice(subjects)
        params = {"q": f"subject:{random_subject}"}

        retry_count = 0
        while retry_count < max_retries:
            response = requests.get(base_url, params=params)

            if response.status_code == 200:
                data = response.json()
                items = data.get("items", [])

                if items:
                    random_book = random.choice(items)
                    
                    book_id = random_book.get("id", "N/A")
                    title = random_book.get("volumeInfo", {}).get("title", "N/A")
                    authors = random_book.get("volumeInfo", {}).get("authors", ["N/A"])

                    return Book(book_id, title, authors)
                else:
                    print("No books found.")
                    return None
            else:
                print(f"Error: {response.status_code}")
                retry_count += 1
                print(f"Retrying... (Attempt {retry_count}/{max_retries})")
                time.sleep(2)

        print(f"Failed to establish a connection after {max_retries} attempts. Exiting.")
        return None
        
class Ebook:
    def __init__(self, book_id, title, authors):
        self.id = book_id
        self.title = title
        self.authors = authors

    @staticmethod
    def get_random_epub_book(max_retries=3):
        base_url = "https://www.googleapis.com/books/v1/volumes"
        subjects = ["fiction", "history", "science", "biography", "fantasy"]
        random_subject = random.choice(subjects)
        params = {"q": f"subject:{random_subject}"}

        retry_count = 0
        while retry_count < max_retries:
            response = requests.get(base_url, params=params)

            if response.status_code == 200:
                data = response.json()
                items = data.get("items", [])

                if items:
                    epub_books = [item for item in items if "epub" in item.get("accessInfo", {}) and item["accessInfo"]["epub"]["isAvailable"]]
                    if epub_books:
                        random_epub_book = random.choice(epub_books)

                        book_id = random_epub_book.get("id", "N/A")
                        title = random_epub_book.get("volumeInfo", {}).get("title", "N/A")
                        authors = random_epub_book.get("volumeInfo", {}).get("authors", ["N/A"])

                        return Ebook(book_id, title, authors)
                    else:
                        print("No EPUB available books found.")
                        return None
                else:
                    print("No results found.")
                    return None
            else:
                print(f"Error: {response.status_code}")
                retry_count += 1
                print(f"Retrying... (Attempt {retry_count}/{max_retries})")
                time.sleep(2)  

        print(f"Failed to establish a connection after {max_retries} attempts. Exiting.")
        return None


    
    
class Room:
    def __init__(self, number, capacity):
        self.number = number
        self.capacity = capacity
        self.reservations=[]
    
    def to_dict(self):
        return {
            "tipo":"Room",
            "number": self.number,
            "capacity": self.capacity,
        }
    
    
    def is_available(self, start_time, end_time):
        for reservation in self.reservations:
            if not (end_time <= reservation.start_time or start_time >= reservation.end_time):
                return False 
        return True

    def reserve(self, user, start_time, end_time, reservation_id):
        self.reservations.append(ReservationRoom(self, user, "reserved", start_time, end_time, reservation_id))

class ReservationRoom:
    def __init__(self, room, user, status, start_time, end_time, reservation_id):
        self.room = room
        self.user = user 
        self.status = status
        self.start_time = start_time
        self.end_time = end_time
        self.reservation_id = reservation_id
        
    def __str__(self):
        return f"Sala {self.room.number} reservada por {self.user.name} (id:{self.user.user_id}) em {self.start_time} até {self.end_time}"

    def to_dict(self):
        return {
            "tipo":"ReservationRoom",
            "room": self.room.number,
            "user": self.user.user_id,
            "status": self.status,
            "start_time": str(self.start_time),
            "end_time": str(self.end_time),
            "reservation_id": self.reservation_id,
        }

class ReservationBook:
    def __init__(self, book, user, status, start_time, end_time, reservation_id):
        self.book = book
        self.user = user
        self.status = status
        self.start_time = start_time
        self.end_time = end_time
        self.reservation_id = reservation_id

    def to_dict(self):
        return {
            "tipo":"ReservationBook",
            "reservation_id": self.reservation_id,
            "user": self.user.user_id,
            "status": self.status,
            "start_time": str(self.start_time),
            "end_time": str(self.end_time),
            "book_id": self.book.id,
            "title": self.book.title,
            "authors": self.book.authors
        }
   
    def __str__(self) -> str:
        return f"Livro {self.book.title} reservado por {self.user.name} (id:{self.user.user_id}) em {self.start_time} até {self.end_time}"
        

class ReservationEbook:
    def __init__(self, ebook, user, status, start_time, end_time, reservation_id):
        self.ebook = ebook
        self.user = user
        self.status = status
        self.start_time = start_time
        self.end_time = end_time
        self.reservation_id = reservation_id

    def to_dict(self):
        return {
            "tipo":"ReservationEbook",
            "reservation_id": self.reservation_id,
            "user": self.user.user_id,
            "status": self.status,
            "start_time": str(self.start_time),
            "end_time": str(self.end_time),
            "ebook_id": self.ebook.id,
            "ebook_title": self.ebook.title,
            "ebook_authors": self.ebook.authors
        }    
    
    def __str__(self):
        return f"Ebook {self.ebook.title} reservado por {self.user.name} (id:{self.user.user_id}) em {self.start_time} até {self.end_time}"
        
    
class Library:
    def __init__(self) -> None:
       
        self.registered_users = [
            User(user_id=i + 1, name=f"Bibliotecario{i+1}", phonenumber=fake.numerify(text='#########'),
                 email=f"Bibliotecario{i+1}@example.com", 
                 tipo="bibliotecario",
                 birthdate=generate_birthdate(),
                 senha="Olaola123") for i in range(4)
        ]
        self.rooms = [Room(number, capacity=random.randint(5, 20)) for number in range(1, 12)]
        self.reservations=[]
        
      
        
       
        credentials = pika.PlainCredentials('myuser', 'mypassword')
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host='rabbitmq3', port='5672', credentials=credentials))  
        self.channel = self.connection.channel()
        self.channel.queue_delete(queue='reservations')
        self.channel.queue_delete(queue='users')
        self.channel.queue_delete(queue='rooms')
        self.channel.queue_declare(queue='reservations')
        self.channel.queue_declare(queue='users')
        self.channel.queue_declare(queue='rooms')
        
        self.send_initial_users()
        print("\n")
        self.send_initial_rooms()
        print("\n")
        self.generate_10_users()
        print("\n")
        
         
    def send_initial_rooms(self):
        for room in self.rooms:
            room_data = room.to_dict()
            message = json.dumps(room_data)
            print(f"Room: {message}")
            self.channel.basic_publish(exchange='', routing_key='rooms', body=message)
        
        
    def send_initial_users(self):
        for user in self.registered_users:
            user_data = user.to_dict()
            message = json.dumps(user_data)
            print(f"User: {message}")
            self.channel.basic_publish(exchange='', routing_key='users', body=message)
        
        
    def generate_new_user(self):
        new_user = User(user_id=len(self.registered_users) + 1,
                        name=fake.user_name(),
                        email=fake.email(),
                        tipo="normaluser",
                        senha=None,
                        phonenumber=fake.numerify(text='#########'),
                        birthdate=generate_birthdate())
        self.registered_users.append(new_user)
        user_data=new_user.to_dict()
        message=json.dumps(user_data)
        print(f"User Criado: {message}")
        self.channel.basic_publish(exchange='',routing_key='users',body=message)
        return new_user
    
    def generate_10_users(self):
        for _ in range(10):
            self.generate_new_user()
            print("\n")
            time.sleep(2)
    

    def generate_and_send_reservations(self):
       
        while True:
            random_choice = random.choices(["user", "book", "ebook", "room"], weights=[0.2, 0.3, 0.3, 0.2])[0]

            if random_choice == "book":
                self.generate_and_send_book_reservation()
            elif random_choice == "ebook":
                self.generate_and_send_ebook_reservation()
            elif random_choice == "room":
                self.generate_and_send_room_reservation()
            elif random_choice == "user":
                self.generate_new_user()
            print("\n")
            time.sleep(3)


    def generate_and_send_book_reservation(self):
        user = random.choice(self.registered_users)
        book = Book.get_random_book()

        current_time = datetime.now()
        start_time = current_time + timedelta(minutes=(15 - current_time.minute % 15))
        start_time = max(start_time, current_time + timedelta(minutes=15))
        start_time = start_time.replace(minute=(start_time.minute // 15) * 15, second=0, microsecond=0)
        start_time = start_time.replace(second=0)

        random_days = random.randint(0, 3)
        start_time += timedelta(days=random_days)

        random_hours = random.randint(8, 23)
        random_minutes = random.choice([0, 15, 30, 45])
        start_time = start_time.replace(hour=random_hours, minute=random_minutes)

        random_duration = random.randint(1, 4)
        end_time = start_time + timedelta(hours=random_duration)
        end_time = min(end_time, start_time + timedelta(weeks=2)) 

        if any(isinstance(reservation, ReservationBook) and reservation.book.id == book.id and reservation.status == "reserved" for reservation in self.reservations):
                print(f"O livro '{book.title}' já está reservado. Não é possível realizar outra reserva.")
                return
        
        reservation_id = len(self.reservations) + 1
        reservation = ReservationBook(book, user, "reserved", start_time, end_time, reservation_id)

        message = json.dumps(reservation.to_dict())
        print(f"Reserva de Livro: {message}\n")
        self.channel.basic_publish(exchange='', routing_key='reservations', body=message)
        self.reservations.append(reservation)


    def generate_and_send_ebook_reservation(self):
        user = random.choice(self.registered_users)
        ebook = Ebook.get_random_epub_book()

        current_time = datetime.now()
        start_time = current_time + timedelta(minutes=(15 - current_time.minute % 15))
        start_time = max(start_time, current_time + timedelta(minutes=15))
        start_time = start_time.replace(minute=(start_time.minute // 15) * 15, second=0, microsecond=0)
        start_time = start_time.replace(second=0)

        random_days = random.randint(0, 3)
        start_time += timedelta(days=random_days)

        random_hours = random.randint(8, 23)
        random_minutes = random.choice([0, 15, 30, 45])
        start_time = start_time.replace(hour=random_hours, minute=random_minutes)

        random_duration = random.randint(1, 4)
        end_time = start_time + timedelta(hours=random_duration)
        end_time = min(end_time, start_time + timedelta(weeks=2)) 

        reservation_id = len(self.reservations) + 1
        reservation = ReservationEbook(ebook, user, "reserved", start_time, end_time, reservation_id)

        message = json.dumps(reservation.to_dict())
        print(f"Reserva de eBook: {message}\n")
        self.channel.basic_publish(exchange='', routing_key='reservations', body=message)
        self.reservations.append(reservation)
    
    def generate_and_send_room_reservation(self):
        user = random.choice(self.registered_users)
        room = random.choice(self.rooms)

        current_time = datetime.now()
        start_time = current_time + timedelta(minutes=(15 - current_time.minute % 15))
        start_time = max(start_time, current_time + timedelta(minutes=15))
        start_time = start_time.replace(minute=(start_time.minute // 15) * 15, second=0, microsecond=0)
        start_time = start_time.replace(second=0)

        random_days = random.randint(0, 3)
        start_time += timedelta(days=random_days)

        random_hours = random.randint(8, 23)
        random_minutes = random.choice([0, 15, 30, 45])
        start_time = start_time.replace(hour=random_hours, minute=random_minutes)

        random_duration = random.randint(1, 4)
        end_time = start_time + timedelta(hours=random_duration)
        end_time = min(end_time, datetime.combine(start_time.date(), datetime.strptime('23:59', '%H:%M').time()))

        if room.is_available(start_time, end_time):
            reservation_id = len(self.reservations) + 1
            room.reserve(user, start_time, end_time, reservation_id)

            message = json.dumps(room.reservations[-1].to_dict())
            print(f"Reserva de Sala: {message}\n")
            self.channel.basic_publish(exchange='', routing_key='reservations', body=message)
            self.reservations.append(room.reservations[-1]) 
    
    
def generate_birthdate():
       
        birthdate = fake.date_of_birth(tzinfo=None, minimum_age=18, maximum_age=100)

        while datetime.now().date() - birthdate < timedelta(days=18 * 365) or datetime.now().date() - birthdate > timedelta(days=100 * 365):
            birthdate = fake.date_of_birth(tzinfo=None, minimum_age=18, maximum_age=100)

        return birthdate
    



if __name__ == "__main__":
    library = Library()
    reservation_thread = Thread(target=library.generate_and_send_reservations)
    reservation_thread.start()
    reservation_thread.join()
