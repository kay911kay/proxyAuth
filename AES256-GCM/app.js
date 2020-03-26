// Confidentiality, Integrity, Authenticity
// Using GCM with AES-256, you don't need to worry about using MACS, HMACS
// GCM would take care of everything. 

const crypto = require('crypto');

const alice = crypto.createECDH('secp256k1');
alice.generateKeys();
const bob = crypto.createECDH('secp256k1');
bob.generateKeys();

const alicePublicKeyBase64 = alice.getPublicKey().toString('base64');
const bobPublicKeyBase64 = bob.getPublicKey().toString('base64');
const aliceSharedKey = alice.computeSecret(bobPublicKeyBase64, 'base64', 'hex');
const bobSharedKey = bob.computeSecret(alicePublicKeyBase64, 'base64', 'hex');

console.log('Alice Public Key: ', alicePublicKeyBase64);
console.log('Bob Public Key: ', bobPublicKeyBase64, "\n");
console.log(aliceSharedKey === bobSharedKey);
console.log('Alice shared Key: ', aliceSharedKey);
console.log('Bob shared Key: ', bobSharedKey, "\n");


//Alice begins here
const MESSAGE = 'SECURE MESSAGE';
//IV - salt of the encyption, random 16 bytes and public, only used one time per messae and append to the encrypted text
const IV = crypto.randomBytes(16); 
//since shared key in HEX, convert it into buffer
const cipher = crypto.createCipheriv('aes-256-gcm', Buffer.from(aliceSharedKey, 'hex'), IV); 
let encrypted = cipher.update(MESSAGE, 'utf8', 'hex');
encrypted += cipher.final('hex');
const auth_tag = cipher.getAuthTag().toString('hex');


//output -IV - 16 bytes = 128 bits,  32 HEXA decimal characters * 4 bit for each character = 128 bits
//       -auth_tag again is again 32 HEXA characters, which means 16 bytes. 
console.log({ IV: IV.toString('hex'), encrypted: encrypted, auth_tag: auth_tag });
//console.table({ IV: IV.toString('hex'), encrypted: encrypted, auth_tag: auth_tag });


const payload = IV.toString('hex') + encrypted + auth_tag;
const payload64 = Buffer.from(payload, 'hex').toString('base64');
console.log(payload64, "\n"); //Bob will decrypt this payload

//Bob begins here
const bob_payload = Buffer.from(payload64, 'base64').toString('hex'); //calculate HEXA from payload base64

const bob_iv = bob_payload.substr(0, 32);   
const bob_encrypted = bob_payload.substr(32, bob_payload.length - 32 - 32); //from 32 to length in 64
const bob_auth_tag = bob_payload.substr(bob_payload.length - 32, 32);

//console.table({ bob_iv, bob_encrypted, bob_auth_tag });
console.log({ bob_iv, bob_encrypted, bob_auth_tag }); console.log();

try {
  const decipher = crypto.createDecipheriv(
    'aes-256-gcm',
    Buffer.from(bobSharedKey, 'hex'),
    Buffer.from(bob_iv, 'hex'));

  //decipher.setAuthTag(Buffer.from(bob_auth_tag, 'hex'));
  decipher.setAuthTag(cypto.randomBytes(16)); //random 16 bytes auth_tag, should through an error 
  let decrypted = decipher.update(bob_encrypted, 'hex', 'utf8');
  decrypted += decipher.final('utf8');

  console.log({ DecyptedMessage: decrypted });

} catch (error) {
  console.log("Unable to authenticate data");
  console.log(error.message);
}
