picrypt
=======

Picrypt is a Java steganography program that will encrypt/decrypt a file and embed/extract it from an image.  It uses RSA public key encryption so a file can be encrypted to a specific person.  The RSA is used to encrypt the AES key that the file is actually encrypted with.  Private keys are stored AES encrypted using a user specified password.

Compile with: javac Picrypt.java picrypt/*.java

Then run with: java Picrypt
