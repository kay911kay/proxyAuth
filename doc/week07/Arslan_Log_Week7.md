# WEEK 7 LOG
#### Author: Arslan Ahmed Qamar
---

## OBJECTIVES
* **Previously,** Setup the UberTooth device, worked on UberTooth different modes including Capturing BLE in WireShark and repeater mode (includes a lot of setup and testing)
* Setup for Elliptic-curve Diffie–Hellman (ECDH) + Authenticated Encryption using AES-256-GCM.  


## SOURCES USED 
* https://www.tecmint.com/configure-pam-in-centos-ubuntu-linux/
* https://people.csail.mit.edu/albert/bluez-intro/
* https://raspberrypi.stackexchange.com/questions/41776/failed-to-connect-to-sdp-server-on-ffffff000000-no-such-file-or-directory
* https://www.lifewire.com/ultimate-windows-7-ubuntu-linux-dual-boot-guide-2200653
* https://linuxize.com/post/how-to-enable-ssh-on-ubuntu-18-04/
* https://github.com/greatscottgadgets/ubertooth/wiki
* https://github.com/greatscottgadgets/ubertooth/issues/194
* https://manpages.debian.org/testing/ubertooth/ubertooth-util.1.en.html
* https://github.com/greatscottgadgets/ubertooth/blob/master/host/doc/ubertooth-util.md
* https://github.com/trulymittal/ECDH-AES-256-GCM

## TODO
* 

---

## Elliptic-Curve Diffie–Hellman (ECDH) + Authenticated Encryption using AES-256-GCM.  
### SETUP

* Install the Node Package Manager 
`npm install`
```
npm WARN saveError ENOENT: no such file or directory, open '/Users/arslanqamar/package.json'
npm notice created a lockfile as package-lock.json. You should commit this file.
npm WARN enoent ENOENT: no such file or directory, open '/Users/arslanqamar/package.json'
npm WARN arslanqamar No description
npm WARN arslanqamar No repository field.
npm WARN arslanqamar No README data
npm WARN arslanqamar No license field.
up to date in 0.496s
```

* npm install in the correct directory
`cd ECDH-AES-256-GCM/`
`npm install`
```
up to date in 0.069s


   ╭──────────────────────────────────────╮
   │                                      │
   │   Update available 5.6.0 → 6.14.3    │
   │      Run npm i -g npm to update      │
   │                                      │
   ╰──────────────────────────────────────╯

npm i -g npm
npm WARN checkPermissions Missing write access to /usr/local/lib/node_modules/npm/node_modules/ansi-regex
npm WARN checkPermissions Missing write access to /usr/local/lib/node_modules/npm/node_modules/aproba
npm WARN checkPermissions Missing write access to /usr/local/lib/node_modules/npm/node_modules/bluebird
npm WARN checkPermissions Missing write access to /usr/local/lib/node_modules/npm/node_modules/cacache/node_modules/ssri
AND goes on ………………….
```
* Lets test now `npm start`
```
> ecdh-aes-256-gcm@1.0.0 start /Users/arslanqamar/git/ECDH-AES-256-GCM
> nodemon app.js

sh: nodemon: command not found
npm ERR! file sh
npm ERR! code ELIFECYCLE
npm ERR! errno ENOENT
npm ERR! syscall spawn
npm ERR! ecdh-aes-256-gcm@1.0.0 start: `nodemon app.js`
npm ERR! spawn ENOENT
npm ERR! 
npm ERR! Failed at the ecdh-aes-256-gcm@1.0.0 start script.
npm ERR! This is probably not a problem with npm. There is likely additional logging output above.
npm WARN Local package.json exists, but node_modules missing, did you mean to install?

npm ERR! A complete log of this run can be found in:
npm ERR!     /Users/arslanqamar/.npm/_logs/2020-03-21T07_48_12_267Z-debug.log
```

* Missing nodemon, install it
`sudo npm i -g npm` `sudo npm install -g nodemon`
```
+ npm@6.14.3
added 326 packages, removed 367 packages and updated 61 packages in 10.379s
```


* npm start, Alice Shared and Bob Shared Key are shown. From now on every single time saving the file would display the new result
```
> ecdh-aes-256-gcm@1.0.0 start /Users/arslanqamar/git/ECDH-AES-256-GCM
> nodemon app.js

[nodemon] 2.0.2
[nodemon] to restart at any time, enter `rs`
[nodemon] watching dir(s): *.*
[nodemon] watching extensions: js,mjs,json
[nodemon] starting `node app.js`
true
Alice shared Key:  fbbfc9ec411ee5e6942eb913e6c2381ab436ffc6e959e403c5eb157fe734c578
Bob shared Key:  fbbfc9ec411ee5e6942eb913e6c2381ab436ffc6e959e403c5eb157fe734c578

[nodemon] clean exit - waiting for changes before restart
```


* Added the Alice IV, encrypted message and auth_tag
```
[nodemon] restarting due to changes...
[nodemon] starting `node app.js`
[nodemon] restarting due to changes...
[nodemon] starting `node app.js`
true
Alice shared Key:  62e92a220053b1998318315e58c400049c999f04862d2f24194046c7da531a47
Bob shared Key:  62e92a220053b1998318315e58c400049c999f04862d2f24194046c7da531a47
{ IV: '23d7dbe9031e74d3af2a9a1b9cc47c4f',
  encrypted: '55ea6c923d256310f706b6d93bcbb32d7e9ee393f961a934ec1d449cc00b',
  auth_tag: '465ff32a80e9b349d4d358d251c64737' }
[nodemon] clean exit - waiting for changes before restart
```

* The last line is the payload that Bob would be decrypting
```
[nodemon] restarting due to changes...
[nodemon] starting `node app.js`
true
Alice shared Key:  ad172b3c1f22728b577e5e71c2baebd740607f9311636cec7d99472f23918a41
Bob shared Key:  ad172b3c1f22728b577e5e71c2baebd740607f9311636cec7d99472f23918a41
{ IV: '9d6afeded5586383073d05dcf5c8df25',
  encrypted: '4992e83c90bf47a8ed75c7bb3a7464cff8ab8df6273ca090199dcc873499',
  auth_tag: '290e8f1e3d79cbf5df9139130af706ea' }
nWr+3tVYY4MHPQXc9cjfJUmS6DyQv0eo7XXHuzp0ZM/4q432JzygkBmdzIc0mSkOjx49ecv135E5Ewr3Buo=
[nodemon] clean exit - waiting for changes before restart
```

* Added the Bob IV, encrypted message and auth_tag
```
[nodemon] starting `node app.js`
true
Alice shared Key:  e18ea99009eca9bb3aa0138c716b0a6bb1eb12d6780e276e28262e579dc5e7ba
Bob shared Key:  e18ea99009eca9bb3aa0138c716b0a6bb1eb12d6780e276e28262e579dc5e7ba
{ IV: 'a6196e8d9da722c7ed82dd26347c1108',
  encrypted: '70a7b71a1ab21591b1a8f9a600789e22d35552ff92e29fd5304a332badba',
  auth_tag: '35ad5c1c4a3f7a2075e52b2b1039be30' }
phlujZ2nIsftgt0mNHwRCHCntxoashWRsaj5pgB4niLTVVL/kuKf1TBKMyutujWtXBxKP3ogdeUrKxA5vjA=
{ bob_iv: 'a6196e8d9da722c7ed82dd26347c1108',
  bob_encrypted: '70a7b71a1ab21591b1a8f9a600789e22d35552ff92e29fd5304a332badba',
  bob_auth_tag: '35ad5c1c4a3f7a2075e52b2b1039be30' }
[nodemon] clean exit - waiting for changes before restart
```
* Finally, Bob will get the SECURE MESSAGE
```
[nodemon] starting `node app.js`
Alice public Key:  BOVwW26/9qQuS0fxKDnnOdT1wu5RVLMAlbEokdm9/d8WNAc3wxjj+xD/ldC0mot/KkdciE4xbwJkpvVP2C5tFWM=
Bob public Key:  BC7/kNqPGSyFn3Z5KF6Hn/DoUcO/N4Z3o5bK51qige8AJ+liynOH6bOFUvmu0pFNdy2NkTdndAKKUHwM+i1r/Ec= 

true
Alice shared Key:  15eb30a34ae8daf751c8dff664ed2af1c6b6b4312c0e8f4757c909419ce8ed58
Bob shared Key:  15eb30a34ae8daf751c8dff664ed2af1c6b6b4312c0e8f4757c909419ce8ed58 

{ IV: '310620a3aa0e976cdbad9229a329fdb6',
  encrypted: '84debc4d6b5d6de6acb5e3073d22',
  auth_tag: 'b05b11308aec305b84d48a30d5cbdf11' }
MQYgo6oOl2zbrZIpoyn9toTevE1rXW3mrLXjBz0isFsRMIrsMFuE1Iow1cvfEQ== 

{ bob_iv: '310620a3aa0e976cdbad9229a329fdb6',
  bob_encrypted: '84debc4d6b5d6de6acb5e3073d22',
  bob_auth_tag: 'b05b11308aec305b84d48a30d5cbdf11' }
{ DecyptedMessage: 'SECURE MESSAGE' }
[nodemon] clean exit - waiting for changes before restart
```

* If the try/catch block won't work, it would say that Unable to authenticatie data or DecyptedMessage is not defined
```
[nodemon] restarting due to changes...
[nodemon] starting `node app.js`
[nodemon] restarting due to changes...
[nodemon] starting `node app.js`
Alice Public Key:  BBEd8dgcD33ie5Dxn94O/xDrYg98UDzS+/hsZ6fZ99iFBbG+0U5sgmIq+eYPqRDJSUGd17w9esIbrcy8Ig7PRiA=
Bob Public Key:  BPM9pTuzuv2e71330zdIfNXuxn4V+CsBWuwvXxmv8GLyuqIrbyx8VQutV7SpN/lQxJQ9UcGKhHsmxbMCvwea3Es= 

true
Alice Shared Key:  430dc4daf8d34b7be4877a5e0808323eb32606d73f8d72fe5f8a9d30306925c2
Bob Shared Key:  430dc4daf8d34b7be4877a5e0808323eb32606d73f8d72fe5f8a9d30306925c2 

{ IV: 'b658d4e5fda3b243107e29e2c371ad48',
  encrypted: 'f12d70fda2cc49228ffbfd8f2dcf',
  auth_tag: 'c1219c61bc128afde3e744c7743b9e86' }
tljU5f2jskMQfiniw3GtSPEtcP2izEkij/v9jy3PwSGcYbwSiv3j50THdDuehg== 

{ bob_iv: 'b658d4e5fda3b243107e29e2c371ad48',
  bob_encrypted: 'f12d70fda2cc49228ffbfd8f2dcf',
  bob_auth_tag: 'c1219c61bc128afde3e744c7743b9e86' }
DecyptedMessage is not defined
[nodemon] clean exit - waiting for changes before restart
```

* For more detailed comments and explanation, please check the `app.js` file







