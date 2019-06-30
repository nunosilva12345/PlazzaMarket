import requests, sys, os, getpass, urllib.parse

class URLSession(requests.Session):
    def __init__(self, prefix_url = None, *args, **kwargs):
        super(URLSession, self).__init__(*args, **kwargs)
        self.prefix_url = prefix_url

    def request(self, method, url, *args, **kwargs):
        return super(URL, self).request(method, urllib.parse.urljoin(self.prefix_url, url), *args, **kwargs)

class Holder:
    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def json(self):
        return { key : value if type(value) != Holder else value.json() for key, value in self.__dict__.items() }

def clear():
    os.system('cls')

def create(session):
    failed = exited = False

    while not exited:
        clear()

        if failed:
            print('Invalid option! Please choose one of the following options:')
            print()

        print('1 - Create Category')
        print('2 - Back')
        
        option = input('--> ')
        failed = not option.isdigit() or not (1 <= int(option) <= 2)

        if not failed:
            exited, option = True, int(option)
            
            if option == 1:
                pass

def delete(session):
    failed = exited = False

    while not exited:
        clear()

        if failed:
            print('Invalid option! Please choose one of the following options:')
            print()

        print('1 - Delete Category')
        print('2 - Delete Consumer')
        print('3 - Delete Producer')
        print('4 - Delete Product')
        print('5 - Back')
        
        option = input('--> ')
        failed = not option.isdigit() or not (1 <= int(option) <= 5)

        if not failed:
            exited, option = True, int(option)
            
            if option == 1:
                pass
            elif option == 2:
                pass
            elif option == 3:
                pass
            elif option == 4:
                pass

def display(session):
    failed = exited = False

    while not exited:
        clear()

        if failed:
            print('Invalid option! Please choose one of the following options:')
            print()

        print('1 - List Categories')
        print('2 - List Consumers')
        print('3 - List Producers')
        print('4 - List Products')
        print('5 - List Receipts')
        print('6 - List Sales')
        print('7 - Back')
        
        option = input('--> ')
        failed = not option.isdigit() or not (1 <= int(option) <= 7)

        if not failed:
            exited, option = True, int(option)
            
            if option == 1:
                pass
            elif option == 2:
                pass
            elif option == 3:
                pass
            elif option == 4:
                pass
            elif option == 5:
                pass
            elif option == 6:
                pass

def main(*argv):
    failed = exited = logged = False
    while not exited:
        output = 'Insert your authentication credentials!'
        if failed:
            output = 'Authentication credentials incorrect! {}'.format(output)
        
        clear()
        print(output)
        user = Holder(username = input('Username: '), password = getpass.getpass())

        # session = URLSession(prefix_url = argv[0] if len(argv) >= 1 else 'http://localhost:8080')
        # response = session.post('/api/login', json = user.json())
        # failed = response.status_code != 200

        failed = user.username != 'lengors' or user.password != 'pedroxp1'
        logged = not failed

        while logged:
            clear()

            if failed:
                print('Invalid option! Please choose one of the following options:')
                print()

            print('1 - Create')
            print('2 - Delete')
            print('3 - List')
            print('4 - Logout')
            print('5 - Exit')

            option = input('--> ')
            failed = not option.isdigit() or not (1 <= int(option) <= 5)

            if not failed:
                option = int(option)
                if option == 1:
                    create()
                elif option == 2:
                    delete()
                elif option == 3:
                    display()
                elif option >= 4:
                    logged, exited = False, option == 5
                    # session.close()


if __name__ == "__main__":
    main(*sys.argv[1:])