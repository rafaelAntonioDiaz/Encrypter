# Encrypter
Encrypter and decrypter from command line.

It takes arguments from command line to set encryption or decryption algoritm. Next to command name should come argument.

It uses following commands:

-mode: It has two possibilities enc for encryption or dec for decryption

-key: An integer for setting encryption or decryption algoritm

-data: Mesage in command line to be either encrypted or decrypted

-out: Route and name of file where to save encrypted or decrypted message

-in: Route and name of file where is the message to be either encrypted or decrypted

-alg: It has two possibilities unicode or shift.

unicode implements unicode algoritm: adds key to each character in messaje and returns encrypted message or substracts ket to decrypt.
shift implements  Caesar's algoritm:  encodes only English letters (from 'a' to 'z' as the first circle and from 'A' to 'Z' as the second circle i.e. after 'z' comes 'a' and after 'Z" comes 'A').
Special characters are kept the same.

Examples:

java Main -mode enc -in road_to_treasure.txt -out protected.txt -key 5 -alg unicode

Encrypts message stores in road_to_treasure.txt to protected.txt, applying a key of 5 and unicode algoritm.

java Main -mode enc -key 5 -data "Welcome to hyperskill!" -alg unicode

Encrypts Welcome to hyperskill!, applying a key of 5 and unicode algoritm.

Answers: \jqhtrj%yt%m~ujwxpnqq&

java Main -key 5 -alg shift -data "Welcome to hyperskill!" -mode enc

Encrypts Welcome to hyperskill!, applying a key of 5 and Caesar's algoritm.

Answers: Bjqhtrj yt mdujwxpnqq!

java Main -key 5 -alg shift -data "Bjqhtrj yt mdujwxpnqq!" -mode dec

Dencrypts Bjqhtrj yt mdujwxpnqq!, applying a key of 5 and Caesar's algoritm.

Answers: Welcome to hyperskill!

