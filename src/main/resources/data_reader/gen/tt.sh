#!/bin/zsh

# Python 환경에서 실행되는 스크립트 작성
python3 << END_OF_PYTHON

from openpyxl import Workbook
import random
import string

# Workbook 생성
wb = Workbook()

# Active Sheet 선택
ws = wb.active

# 데이터 생성 및 저장
for i in range(1, 20):
# for i in range(1, 1000001):
    row_data = []
    # 첫 번째 열은 문자 2개와 숫자의 조합
    random_chars = ''.join(random.choices(string.ascii_letters, k=2))
    row_data.append(f"{random_chars}{i}")
    # 나머지 열은 각각 랜덤한 문자와 숫자의 조합
    for _ in range(5):
        random_char = random.choice(string.ascii_letters)
        random_number = random.randint(0, 1000000)
        row_data.append(f"{random_char}{random_number}")
    ws.append(row_data)

# xlsx 파일 저장
wb.save("temp_data_6_columns_20.xlsx")

END_OF_PYTHON

echo "Temporary data generation complete."
