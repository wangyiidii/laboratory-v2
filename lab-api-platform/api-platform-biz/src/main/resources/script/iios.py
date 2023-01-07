import time
import random
import base64
import requests
import json
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_v1_5 as Cipher_pkcs1_v1_5
import argparse


def aes_encrypt(plaintext, key):
    cipher = AES.new(key=key.encode(), mode=AES.MODE_ECB)
    buf = cipher.encrypt(pad(plaintext.encode(), 16))
    return base64.b64encode(buf).decode('utf8')


def aes_decrypt(ciphertext, key):
    cipher = AES.new(key=key.encode(), mode=AES.MODE_ECB)
    buf = cipher.decrypt(base64.b64decode(ciphertext))
    return unpad(buf, 16).decode('utf-8')


# 生成16位大小写字母数字字符串
def get_random_key():
    ret = ''
    arr = list('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789')
    for i in range(16):
        ret = ret + arr[random.randint(0, 61)]
    return ret


def rsa_encrypt(str):
    public_key = 'MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgE8/mRyYJwyMjSGNL9ClZzkly2+SoSXiPcyH6t2sfmgpgJEn9uuQRG+VeBIaAurtfkGxwb+gzY2dEJED1KhZtj/H5koPhZq5MnJuAEDE6YlL61ELJY5PPRWPl2MO5aWsaX32dfXlrdDsKx+UlLbwDjagMVo0Z/GiODO6yGbYp8wZAgMBAAE='
    key = "-----BEGIN RSA PUBLIC KEY-----\n" + public_key + "\n-----END RSA PUBLIC KEY-----"
    rsakey = RSA.importKey(key)
    cipher = Cipher_pkcs1_v1_5.new(rsakey)  # 创建用于执行pkcs1_v1_5加密或解密的密码
    ret = cipher.encrypt(str.encode('utf-8'))
    return base64.b64encode(ret).decode('utf-8')


def sign(key):
    return rsa_encrypt(key + '.' + str(int(time.time() * 1000)))


def post(url, data, token=None):
    key = get_random_key()
    headers = {
        'sign': sign(key),
        'content-type': 'text/plain',
        'origin': 'https://iios.ga',
        'referer': 'https://iios.ga',
        'user-agent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1'
    }
    if token is not None:
        headers['authorization'] = 'Basic ' + token
    ciphertext = aes_encrypt(json.dumps(data, ensure_ascii=False, separators=(',', ':')), key)
    ret = requests.post(url=url, data=ciphertext, headers=headers).text
    plaintext = aes_decrypt(ret, key)
    return json.loads(plaintext)


def login(email, password):
    url = 'https://iios.ga/api/user/login'
    data = {
        'email': email,
        'password': password
    }
    return post(url, data)


def task(token):
    # {'type': 2}
    # {"success":true,"result":{"points":1}}
    url = 'https://iios.ga/api/task'
    data = {
        'type': 2
    }
    return post(url, data, token)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='python argparse demo')
    parser.add_argument('-e', '--email', type=str, required=True)
    parser.add_argument('-p', '--password', type=str, required=True)
    args = parser.parse_args()

    ret = login(args.email, args.password)
    if ret['success'] is False:
        print(ret['message'])
    else:
        token = ret['result']['token']
        ret = task(token)
        if ret['success']:
            print('签到成功')
        else:
            print(ret['message'])
