document.addEventListener('DOMContentLoaded', function () {
    const sections = document.querySelectorAll('section');
    const navLinks = document.querySelectorAll('.menuBar a');

    // 스크롤 이벤트 처리
    window.addEventListener('scroll', function () {
        let currentSection = '';

        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            if (window.pageYOffset >= sectionTop - 60) {  // 60은 네비게이션 높이를 고려한 값
                currentSection = section.getAttribute('class');
            }
        });

        // 링크 색상 변경
        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${currentSection}`) {
                link.classList.add('active');
            }
        });
    });
});
