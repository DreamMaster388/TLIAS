const PAGE_SIZE = 10;

const app = Vue.createApp({
    data() {
        return {
            employees: [],
            total: 0,
            searchName: '',
            searchGender: '',
            searchDepartment: '',
            searchPosition: '',
            currentPage: 1,
            jumpPage: null,
            showSearchModal: false,
            deleteTarget: null,
            showDeleteModal: false,
            showAddModal: false,
            addForm: {
                name: '',
                gender: '男',
                department: '',
                position: '',
                entryDate: ''
            }
        }
    },
    computed: {
        totalPages() {
            return Math.ceil(this.total / PAGE_SIZE);
        },
        pageData() {
            return this.employees;
        }
    },
    methods: {
        openSearchModal() {
            this.showSearchModal = true;
        },
        closeSearchModal() {
            this.showSearchModal = false;
        },
        fetchEmployees() {
            const params = new URLSearchParams();
            if (this.searchName) params.append('name', this.searchName);
            if (this.searchGender) params.append('gender', this.searchGender);
            if (this.searchDepartment) params.append('department', this.searchDepartment);
            if (this.searchPosition) params.append('position', this.searchPosition);
            params.append('page', this.currentPage);
            params.append('size', PAGE_SIZE);

            fetch(`/api/employees?${params.toString()}`)
                .then(res => res.json())
                .then(result => {
                    this.employees = result.data;
                    this.total = result.total;
                });
        },
        clear() {
            this.searchName = '';
            this.searchGender = '';
            this.searchDepartment = '';
            this.searchPosition = '';
        },
        search() {
            this.currentPage = 1;
            this.showSearchModal = false;
            this.fetchEmployees();
        },
        goPage(p) {
            if (typeof p !== 'number' || p < 1 || p > this.totalPages) return;
            this.currentPage = p;
            this.fetchEmployees();
        },
        jumpToPage() {
            const p = this.jumpPage;
            if (p === null || p === '' || isNaN(p)) return;
            this.goPage(p);
            this.jumpPage = null;
        },
        deleteRow(d) {
            this.deleteTarget = d;
            this.showDeleteModal = true;
        },
        confirmDelete() {
            const id = this.deleteTarget.id;
            fetch(`/api/employees/${id}`, { method: 'DELETE' })
                .then(() => {
                    this.showDeleteModal = false;
                    this.deleteTarget = null;
                    this.fetchEmployees();
                });
        },
        cancelDelete() {
            this.showDeleteModal = false;
            this.deleteTarget = null;
        },
        editRow(d) {
            alert('编辑 ' + d.name);
        },
        openAddModal() {
            this.addForm = { name: '', gender: '男', department: '', position: '', entryDate: '' };
            this.showAddModal = true;
        },
        closeAddModal() {
            this.showAddModal = false;
        },
        saveEmployee() {
            if (!this.addForm.name) return;
            fetch('/api/employees', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.addForm)
            }).then(() => {
                this.showAddModal = false;
                this.fetchEmployees();
            });
        }
    },
    mounted() {
        this.fetchEmployees();
    }
});

app.mount('#app');
