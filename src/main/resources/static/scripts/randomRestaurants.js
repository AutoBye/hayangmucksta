document.getElementById('randomRestaurantBtn').addEventListener('click', function () {
    const modal = document.getElementById('restaurantModal');
    const loading = document.getElementById('loading');
    const restaurantName = document.getElementById('restaurantName');
    const restaurantAddress = document.getElementById('restaurantAddress');
    const menuList = document.getElementById('menuList'); // 메뉴 리스트를 표시할 div

    // 로딩 메시지 표시
    loading.style.display = 'none';
    menuList.innerHTML = ''; // 메뉴 리스트 초기화

    // 서버로부터 랜덤 가게 정보를 요청
    fetch('/restaurants/random')
        .then(response => response.json())
        .then(data => {
            // 로딩 메시지 숨기기
            loading.style.display = 'none';

            // 가게 정보 표시
            restaurantName.innerText = `가게 이름: ${data.name}`;
            restaurantAddress.innerText = `주소: ${data.address}`;

            // 메뉴 정보 표시
            data.menus.forEach(menu => {
                const menuItem = document.createElement('p');
                menuItem.innerText = `메뉴: ${menu.menuName}, 가격: ${menu.price}, 설명: ${menu.description}`;
                menuList.appendChild(menuItem);
            });

            const modal = document.getElementById('randomRestaurantModal');
            modal.style.display = 'flex';  // 모달 표시
            // 모달 표시
        })
        .catch(error => {
            loading.style.display = 'none';
            alert('가게 정보를 불러오는 중 오류가 발생했습니다.');
        });
});

// 모달을 클릭하여 닫기
window.onclick = function(event) {
    const modal = document.getElementById('restaurantModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};

// 모달 닫기 버튼 동작
document.querySelector('.close').addEventListener('click', function () {
    const modal = document.getElementById('randomRestaurantModal');
    modal.style.display = 'none';  // 모달 숨기기
});

// 모달 바깥 클릭 시 닫기
window.addEventListener('click', function (event) {
    const modal = document.getElementById('randomRestaurantModal');
    if (event.target === modal) {
        modal.style.display = 'none';  // 모달 숨기기
    }
});

document.getElementById('recommendRestaurantBtn').addEventListener('click', function () {
    const recommendationResultDiv = document.getElementById('recommendationResult');
    const recommendLoading = document.getElementById('recommendLoading');
    const recommendationResultContent = document.getElementById('recommendationResultContent');
    const recommendedMenuList = document.getElementById('recommendedMenuList');


    // 모달을 보여주고 로딩 메시지를 띄움
    recommendationResultDiv.style.display = 'flex';
    recommendLoading.style.display = 'flex';
    recommendationResultContent.style.display = 'none'; // 로딩 중이므로 일단 숨김


    // 서버로부터 추천 데이터를 요청 (POST 메서드로 변경)
    fetch('/recommend-based-on-ingredients', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            // HTTP 상태 코드를 확인하여 처리
            if (!response.ok) {
                if (response.status === 401) {
                    // 로그인 필요 시 로그인 페이지로 리다이렉트
                    window.location.href = '/login'; // 로그인 페이지 경로
                    return;
                } else {
                    throw new Error('추천을 가져오는 중 오류가 발생했습니다.');
                }
            }



            return response.json();
        })
        .then(data => {
            // 로딩 메시지 숨기고 결과 표시
            recommendLoading.style.display = 'none';
            recommendationResultContent.style.display = 'block';

            // data와 recommended_menus 존재 여부 확인
            if (!data || !data.recommended_menus || data.recommended_menus.length === 0) {
                recommendedMenuList.innerHTML = '<li>추천 결과가 없습니다.</li>';
                return;
            }

            // 결과 표시
            recommendedMenuList.innerHTML = ''; // 기존 내용 초기화
            data.recommended_menus.forEach(menu => {
                const li = document.createElement('li');
                const a = document.createElement('a');
                if (menu.restaurant_id) {
                    a.href = `/restaurants/${menu.restaurant_id}`;
                } else {
                    a.href = "#";
                }
                a.innerText = `Menu: ${menu.menu}, Restaurant: ${menu.restaurant}, Score: ${menu.score}`;
                li.appendChild(a);
                recommendedMenuList.appendChild(li);
            });
        })
        .catch(error => {
            // 로딩 메시지 숨기고 에러 메시지 표시
            recommendLoading.style.display = 'none';
            recommendationResultContent.style.display = 'none'; // 에러 발생 시 결과 내용 숨김
            alert(error.message); // 에러 메시지 표시
        });
});

// 닫기 버튼 기능
document.querySelector('.close2').addEventListener('click', function () {
    document.getElementById('recommendationResult').style.display = 'none';
});

// 모달을 클릭하여 닫기
window.onclick = function(event) {
    const modal = document.getElementById('recommendationResult');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};

// 모달 바깥 클릭 시 닫기
window.addEventListener('click', function (event) {
    const modal = document.getElementById('recommendationResult');
    if (event.target === modal) {
        modal.style.display = 'none';  // 모달 숨기기
    }
});


document.addEventListener('DOMContentLoaded', function () {
    const recommendationResultDiv = document.getElementById('recommendationResult');
    const recommendLoading = document.getElementById('recommendLoading');
    const recommendationResultContent = document.getElementById('recommendationResultContent');

    // 뒤로 가기 시 모달창을 닫기
    recommendationResultDiv.style.display = 'none';
    recommendLoading.style.display = 'none';
    recommendationResultContent.style.display = 'none';
});
