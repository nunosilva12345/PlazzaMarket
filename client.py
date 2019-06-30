import requests, sys, os, getpass, urllib.parse


class URLSession(requests.Session):
    def __init__(self, prefix_url=None, *args, **kwargs):
        super(URLSession, self).__init__(*args, **kwargs)
        self.prefix_url = prefix_url

    def request(self, method, url, *args, **kwargs):
        return super(URLSession, self).request(
            method, urllib.parse.urljoin(self.prefix_url, url), *args, **kwargs
        )


class Holder:
    def __init__(self, **kwargs):
        self.__dict__.update(kwargs)

    def json(self):
        return {
            key: value if type(value) != Holder else value.json()
            for key, value in self.__dict__.items()
        }


def clear():
    os.system("cls")


def create(session: URLSession):
    failed = exited = False

    while not exited:
        clear()

        if failed:
            print("Invalid option! Please choose one of the following options:")
            print()

        print("1 - Create Category")
        print("2 - Back")

        option = input("--> ")
        failed = not option.isdigit() or not (1 <= int(option) <= 2)

        if not failed:
            exited, option = True, int(option)

            if option == 1:
                pass


def delete(session: URLSession):
    failed = exited = False

    while not exited:
        clear()

        if failed:
            print("Invalid option! Please choose one of the following options:")
            print()

        print("1 - Delete Category")
        print("2 - Delete Consumer")
        print("3 - Delete Producer")
        print("4 - Delete Product")
        print("5 - Back")

        option = input("--> ")
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


def display(session: URLSession):
    failed = exited = False

    while not exited:
        clear()

        if failed:
            print("Invalid option! Please choose one of the following options:")
            print()

        print("1 - List Categories")
        print("2 - List Consumers")
        print("3 - List Producers")
        print("4 - List Products")
        print("5 - List Receipts")
        print("6 - List Sales")
        print("7 - Back")

        option = input("--> ")
        failed = not option.isdigit() or not (1 <= int(option) <= 7)

        if not failed:
            exited, option = True, int(option)

            if option == 1:
                response = session.get("/api/category")
                if response.status_code == 200:
                    response = response.json()
                    clear()
                    print("Categories:")
                    for i, category in enumerate(response):
                        print("{} - {}".format(i + 1, category["name"]))
                else:
                    print("Unable to obtain data!")
                print()
                input("Press [ENTER] to continue...")
            elif option == 2:
                # response = session.get('/api/')
                pass
            elif option == 3:
                pass
            elif option == 4:
                response = session.get("/api/products")
                if response.status_code == 200:
                    response = response.json()
                    clear()
                    print("Products:")
                    for i, product in enumerate(response):
                        print(
                            "{} - {} [price = {:.2f}, quantity = {:.2f}]\n\t{}".format(
                                i + 1,
                                product["name"],
                                float(product["price"]),
                                float(product["quantity"]),
                                product["description"],
                            )
                        )
                else:
                    print("Unable to obtain data!")
                print()
                input("Press [ENTER] to continue...")
            elif option == 5:
                pass
            elif option == 6:
                response = session.get("/api/sale/all")
                if response.status_code == 200:
                    response = response.json()
                    clear()
                    print("Sales:")
                    for i, sale in enumerate(response):
                        product = sale['product']
                        print(
                            "{} - {} [price = {:.2f}, quantity = {:.2f}]: {}\n\t{}".format(
                                i + 1,
                                product["name"],
                                float(product["price"]),
                                float(sale["quantity"]),
                                sale['status'],
                                product["description"]
                            )
                        )
                else:
                    print("Unable to obtain data!")
                print()
                input("Press [ENTER] to continue...")


def main(*argv):
    failed = exited = logged = False
    while not exited:
        output = "Insert your authentication credentials!"
        if failed:
            output = "Authentication credentials incorrect! {}".format(output)

        clear()
        print(output)
        user = Holder(username=input("Username: "), password=getpass.getpass())

        session = URLSession(
            prefix_url=argv[0] if len(argv) >= 1 else "http://localhost:8080"
        )
        response = session.post("/api/admin/login", json=user.json())
        failed = response.status_code != 200

        # failed = user.username != 'lengors' or user.password != 'pedroxp1'
        logged = not failed

        while logged:
            clear()

            if failed:
                print("Invalid option! Please choose one of the following options:")
                print()

            print("1 - Create")
            print("2 - Delete")
            print("3 - List")
            print("4 - Logout")
            print("5 - Exit")

            option = input("--> ")
            failed = not option.isdigit() or not (1 <= int(option) <= 5)

            if not failed:
                option = int(option)
                if option == 1:
                    create(session)
                elif option == 2:
                    delete(session)
                elif option == 3:
                    display(session)
                elif option >= 4:
                    logged, exited = False, option == 5
                    session.close()


if __name__ == "__main__":
    main(*sys.argv[1:])
