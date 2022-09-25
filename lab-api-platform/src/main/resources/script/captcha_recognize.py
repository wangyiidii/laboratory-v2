# coding=utf-8

import sys
import ddddocr
import argparse

parser = argparse.ArgumentParser(description='python argparse demo')
parser.add_argument('-p', '--path', type=str, required=True, help='验证码图片文件路径')
args = parser.parse_args()

path = args.path
if not bool(path):
    sys.exit(1)

ocr = ddddocr.DdddOcr(show_ad=False, beta=True)

if bool(path):
    try:
        with open(path, 'rb') as f:
            image = f.read()
        res = ocr.classification(image)
        print(res)
        sys.exit(0)
    except  Exception as e:
        sys.exit(2)
