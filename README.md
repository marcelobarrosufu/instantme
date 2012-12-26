instantme
=========

InstantME is an Instagram client for Nokia S40 phones.
It has been tested on newer Asha phones, touch or touch and type (Asha 305, 306, 308, 309, 311).
Older phones can work as well but they were not fully tested yet.

Features and Limitations
------------------------

Due to Instragram API rules, this application has some restrictions:

* It can not post photos, only the official application is allowed to use such feature. "Photo quality" is the reason behind this restriction (according to Instagram), even you can post photos using crap cameras of some cheap android phone.
* It is now allowed to create an account. You need to have a previously created account.

Some features available:

* Browse your Instagram feed
* Browse your followers and people you are following
* Post/Delete comments and likes

Contribute
----------

You can contribute to this project in several ways:

* Implementing new features
* Fixing bugs
* Enhancing documentation
* Testing this application on other S40 phones
* Making modification in the source code to support other phones
* Sending emails to Instagram, requesting "create account" and "post photos" privileges
* ...

Donate
------

InstantME uses Inneractive banners, displayed in the middle of photo feed. Please, do not remove this feature when creating clones and keep the Inneractive tracking in jad file. It is the main way InstantME can raise some money for machine upgrades, participation in Nokia events and web hosting providers. This application is developed in my free time and it is not supported by any company.

Or you can make a [donation via PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=LLZKNXT92KFQQ&lc=BR&item_name=InstantME&item_number=1&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHosted).

Security
--------

InstantME uses OAuth2 authentication to generate a session ID based on your username and password. Only this session ID and user ID are stored in your phone, avoiding problems with stolen or lost phones.

External libraries used
-----------------------

* [Imagelib](http://projects.developer.nokia.com/imagelib)
* Inneractive library for S40

External java classes used
--------------------------

* Nokia BackStack class
* Nokia DataModel class for Worpress project

License
-------

This work is licensed under a [Creative Commons Attribution-ShareAlike 3.0 Unported License](http://creativecommons.org/licenses/by-sa/3.0/).
