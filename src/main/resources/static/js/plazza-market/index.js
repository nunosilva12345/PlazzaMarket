class Product {
    constructor(name, description, price) {
        Product.counter = Product.counter || 0;
        this.id = ko.observable(Product.counter++);
        this.name = ko.observable(name);
        this.price = ko.observable(price);
        this.description = ko.observable(description);
    }
}

class Item {
    constructor(product, quantity) {
        let self = this;
        this.product = ko.observable(product);
        this.quantity = ko.observable(quantity);
        this.total = ko.computed(function () {
            return self.quantity() * self.product().price();
        });
        this.formattedQuantity = ko.computed(function () {
            return self.quantity() == 1 ? `1 item` : `${self.quantity()} items`;
        });
    }
}

class Container {
    constructor() {
        let self = this;
        this.mapper = ko.observable({});
        this.items = ko.observableArray([]);
        this.totalQuantity = ko.computed(function () {
            let total = 0;
            for (let i = 0; i < self.items().length; i++)
                total += self.items()[i]().quantity();
            return total;
        });
        this.totalPrice = ko.computed(function () {
            let total = 0;
            for (let i = 0; i < self.items().length; i++)
                total += self.items()[i]().total();
            return total;
        });
    }

    add(product, quantity = 1) {
        if (product.id() in this.mapper())
            this.items()[this.mapper()[product.id()]]().quantity(this.items()[this.mapper()[product.id()]]().quantity() + quantity);
        else {
            this.mapper()[product.id()] = this.items().length;
            this.items.push(ko.observable(new Item(product, quantity)));
        }
    }

    remove(product, quantity = 1) {
        if (product.id() in this.mapper()) {
            quantity = Math.min(quantity, this.items()[this.mapper()[product.id()]]().quantity());
            this.items()[this.mapper()[product.id()]]().quantity(this.items()[this.mapper()[product.id()]]().quantity() - quantity);
            if (this.items()[this.mapper()[product.id()]]().quantity() <= 0) {
                this.items.splice(this.mapper()[product.id()], 1);
                delete this.mapper()[product.id()]
            }
        }
    }
}

class FeedView {
    constructor() {
        let self = this;
        this.cart = ko.observable(new Container());
        this.store = ko.observable(new Container());
        this.categories = ko.observableArray([
            ko.observable({ 'name': 'Bulbs' }),
            ko.observable({ 'name': 'Flowers' }),
            ko.observable({ 'name': 'Fruits' }),
            ko.observable({ 'name': 'Fungi' }),
            ko.observable({ 'name': 'Leaves' }),
            ko.observable({ 'name': 'Roots' }),
            ko.observable({ 'name': 'Seeds' }),
            ko.observable({ 'name': 'Tubers' }),
        ]);
        this.companies = ko.observableArray([
            ko.observable({ 'name': 'Company 1' }),
            ko.observable({ 'name': 'Company 2' }),
            ko.observable({ 'name': 'Company 3' }),
        ]);
        for (let i = 0; i < 9; i++)
            this.store().add(new Product('Orange', 'This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.', Math.random() * 10), 9);
        self.buy = function (element) {
            self.store().remove(element.product());
            self.cart().add(element.product());
            if ($('.cart-list').children().length == 0)
                $('.cart-total').css({
                    'border-radius': '.25rem',
                });
            else {
                $('.cart-total').css({
                    'border-top-left-radius': '0',
                    'border-top-right-radius': '0',
                });
                let $list = $('.cart-list');
                $($list.children().get(0)).css({
                    'border-top-right-radius': $list.get(0).scrollHeight > $list.height() ? '0' : '.25rem',
                });
            }
        }
        self.unbuy = function (element) {
            self.cart().remove(element.product());
            self.store().add(element.product());
            if ($('.cart-list').children().length == 0)
                $('.cart-total').css({
                    'border-radius': '.25rem',
                });
            else {
                $('.cart-total').css({
                    'border-top-left-radius': '0',
                    'border-top-right-radius': '0',
                });
                let $list = $('.cart-list');
                $($list.children().get(0)).css({
                    'border-top-right-radius': $list.get(0).scrollHeight > $list.height() ? '0' : '.25rem',
                });
            }
        }
    }
}

$(function () {
    var touchstartX, touchstartY;
    $(window).on('touchstart', function (e) {
        touchstartX = e.originalEvent.touches[0].clientX;
        touchstartY = e.originalEvent.touches[0].clientY;
    }).on('touchend', function (e) {
        let touchendX = e.originalEvent.changedTouches[0].clientX;
        let touchendY = e.originalEvent.changedTouches[0].clientY;
        let diffX = Math.abs(touchendX - touchstartX);
        let diffY = Math.abs(touchendY - touchstartY);
        if (diffX > diffY) {
            if (touchstartX > touchendX + 5) {
                let $next = $('#mobile_tab .nav-link.active').parent().next();
                if ($next.length != 0)
                    $next.children().tab('show');
            } else if (touchstartX < touchendX - 5) {
                let $prev = $('#mobile_tab .nav-link.active').parent().prev();
                if ($prev.length != 0)
                    $prev.children().tab('show');
            }
        }
    });

    $('a.overrider[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        $(this).children().addClass('far').removeClass('fal');
    }).on('hidden.bs.tab', function (e) {
        $(this).children().addClass('fal').removeClass('far');
    });

    $('main[role="main"]').click(function () {
        if ($('.collapse').hasClass('show'))
            $('.navbar-toggler').click();
    });

    $('.cart-total').css({
        'border-radius': '.25rem'
    });

    let listener = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutated) {
            $(mutated.target).css({
                'position': 'relative',
                'transform': 'none'
            });
        });
    });

    let $elements = $('.dropdown.overrider .dropdown-menu.overrider');
    for (let i = 0; i < $elements.length; i++)
        listener.observe($elements.get(i), {
            attributes: true,
            attributeFilter: ['style']
        });

    $('.dropdown.overrider').on({
        'shown.bs.dropdown': function () { this.closable = false; },
        'click': function () { this.closable = false; },
        'hide.bs.dropdown': function () { return this.closable; }
    });

    $('.dropdown.overrider .dropdown-toggle').on('click', function () {
        $(this).parent().get(0).closable = true;
    });

    ko.applyBindings(new FeedView());
});